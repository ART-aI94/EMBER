
package com.example.ember;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GameDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "game_title";
    public static final String EXTRA_IMAGE = "game_image";

    private ImageView coverImage;
    private TextView titleText;
    private TextView metadataText;
    private TextView descriptionText;

    private LinearLayout requirementsLayout;
    private LinearLayout galleryLayout;

    private TextView requirementsContent;
    private TextView galleryCounter;
    private ImageView galleryImage;

    private List<Integer> screenshots;
    private int currentScreenshot = 0;

    private String minimumRequirements;
    private String recommendedRequirements;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ListenerRegistration commentsListener;

    // Комментарии
    private EditText commentInput;
    private Button sendCommentButton;
    private TextView commentsCount;
    private LinearLayout commentsContainer;

    private String currentUid;
    private String currentUsername;
    private String currentAvatarId;
    private String currentGameId;

    private boolean profileLoaded = false;
    private boolean hasOwnComment = false;
    private boolean isSendingComment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadGame();
        initializeComments();
    }

    private void initializeViews() {

        ImageButton backButton = findViewById(R.id.gameDetailsBack);

        coverImage = findViewById(R.id.gameDetailsCover);
        titleText = findViewById(R.id.gameDetailsTitle);

        metadataText = findViewById(R.id.gameDetailsMetadata);
        descriptionText = findViewById(R.id.gameDetailsDescription);

        Button storeButton =
                findViewById(R.id.gameDetailsStoreButton);

        requirementsLayout =
                findViewById(R.id.gameDetailsRequirements);

        galleryLayout =
                findViewById(R.id.gameDetailsGallery);

        requirementsContent =
                findViewById(R.id.requirementsContent);

        galleryImage =
                findViewById(R.id.galleryImage);

        galleryCounter =
                findViewById(R.id.galleryCounter);

        ImageButton previousButton =
                findViewById(R.id.galleryPrevious);

        ImageButton nextButton =
                findViewById(R.id.galleryNext);

        RadioGroup requirementsToggle =
                findViewById(R.id.requirementsToggle);

        // Элементы комментариев
        commentInput = findViewById(R.id.commentInput);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        commentsCount = findViewById(R.id.commentsCount);
        commentsContainer = findViewById(R.id.commentsContainer);

        backButton.setOnClickListener(v -> finish());

        storeButton.setOnClickListener(v -> {
            Object tag = storeButton.getTag();

            if (tag instanceof String) {
                String url = (String) tag;

                if (!url.trim().isEmpty()) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                    );

                    startActivity(intent);
                }
            }
        });

        requirementsToggle.setOnCheckedChangeListener(
                (group, checkedId) -> {

                    if (checkedId == R.id.requirementsMinimum) {
                        requirementsContent.setText(
                                minimumRequirements
                        );
                    } else if (
                            checkedId == R.id.requirementsRecommended
                    ) {
                        requirementsContent.setText(
                                recommendedRequirements
                        );
                    }
                }
        );

        previousButton.setOnClickListener(
                v -> showPreviousScreenshot()
        );

        nextButton.setOnClickListener(
                v -> showNextScreenshot()
        );

        sendCommentButton.setOnClickListener(
                v -> publishComment()
        );

        setupGallerySwipe();
    }

    private void loadGame() {

        Intent intent = getIntent();

        String title = intent.getStringExtra(EXTRA_TITLE);

        int imageResource = intent.getIntExtra(
                EXTRA_IMAGE,
                0
        );

        if (title == null) {
            title = "Неизвестная игра";
        }

        titleText.setText(title);

        if (imageResource != 0) {
            coverImage.setImageResource(imageResource);
        }

        GameDetailsData game =
                GameDetailsRepository.getGame(title);

        if (game == null) {
            return;
        }

        displayMetadata(game);
        displayDescription(game);
        displayStoreButton(game);
        displayRequirements(game);
        displayGallery(game);
    }

    // ------------------------------------
    // ИДЕНТИФИКАТОР ИГРЫ
    // ------------------------------------

    private String getGameId(String title) {

        if (title == null || title.trim().isEmpty()) {
            return null;
        }

        String normalized = title.trim();

        switch (normalized) {
            case "Dark Souls II":
                normalized = "Dark Souls 2";
                break;

            case "Dark Souls III":
                normalized = "Dark Souls 3";
                break;
        }

        return normalized
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");
    }

    // ------------------------------------
    // ИНИЦИАЛИЗАЦИЯ КОММЕНТАРИЕВ
    // ------------------------------------

    private void initializeComments() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null || !user.isEmailVerified()) {
            commentInput.setEnabled(false);
            sendCommentButton.setEnabled(false);

            commentInput.setHint(
                    "Войдите в подтверждённый аккаунт, чтобы комментировать"
            );

            return;
        }

        currentUid = user.getUid();

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        currentGameId = getGameId(title);

        if (currentGameId == null) {
            commentInput.setEnabled(false);
            sendCommentButton.setEnabled(false);
            return;
        }

        // Получаем настоящий профиль пользователя из Firestore
        db.collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (!document.exists()) {
                        showCommentError(
                                "Профиль пользователя не найден."
                        );
                        return;
                    }

                    currentUsername =
                            document.getString("username");

                    currentAvatarId =
                            document.getString("avatarId");

                    if (currentUsername == null
                            || currentAvatarId == null) {

                        showCommentError(
                                "Не удалось загрузить данные профиля."
                        );
                        return;
                    }

                    profileLoaded = true;

                    commentInput.setEnabled(true);
                    sendCommentButton.setEnabled(true);

                    attachCommentsListener();
                })
                .addOnFailureListener(error -> {

                    if (!isFinishing() && !isDestroyed()) {
                        showCommentError(
                                "Не удалось загрузить профиль."
                        );
                    }
                });
    }

    // ------------------------------------
    // ПОДКЛЮЧЕНИЕ REALTIME LISTENER
    // ------------------------------------

    private void attachCommentsListener() {

        if (!profileLoaded
                || currentGameId == null
                || commentsListener != null) {
            return;
        }

        commentsListener = db.collection("games")
                .document(currentGameId)
                .collection("comments")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener(this, (snapshot, error) -> {

                    if (error != null) {
                        showCommentError(
                                "Не удалось загрузить комментарии."
                        );
                        return;
                    }

                    if (snapshot == null) {
                        return;
                    }

                    hasOwnComment = false;

                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        if (document.getId().equals(currentUid)) {
                            hasOwnComment = true;
                            break;
                        }
                    }

                    renderComments(snapshot.getDocuments());
                    updateCommentComposer();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (profileLoaded) {
            attachCommentsListener();
        }
    }

    @Override
    protected void onStop() {

        if (commentsListener != null) {
            commentsListener.remove();
            commentsListener = null;
        }

        super.onStop();
    }

    // ------------------------------------
    // ПОЛЕ ПУБЛИКАЦИИ
    // ------------------------------------

    private void updateCommentComposer() {

        if (hasOwnComment) {

            commentInput.setVisibility(View.GONE);
            sendCommentButton.setVisibility(View.GONE);

            commentInput.setText("");

        } else {

            commentInput.setVisibility(View.VISIBLE);
            sendCommentButton.setVisibility(View.VISIBLE);

            commentInput.setEnabled(profileLoaded);
            sendCommentButton.setEnabled(
                    profileLoaded && !isSendingComment
            );
        }
    }

    private void publishComment() {

        if (!profileLoaded || isSendingComment) {
            return;
        }

        if (hasOwnComment) {
            Toast.makeText(
                    this,
                    "Ты уже оставил комментарий к этой игре.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String text = commentInput.getText()
                .toString()
                .trim();

        if (text.isEmpty()) {
            commentInput.setError("Напиши комментарий");
            return;
        }

        if (text.length() > 1000) {
            commentInput.setError(
                    "Максимум 1000 символов"
            );
            return;
        }

        isSendingComment = true;
        sendCommentButton.setEnabled(false);
        sendCommentButton.setText("Публикация...");

        Map<String, Object> comment = new HashMap<>();

        comment.put("userId", currentUid);
        comment.put("username", currentUsername);
        comment.put("avatarId", currentAvatarId);
        comment.put("text", text);
        comment.put("createdAt", FieldValue.serverTimestamp());

        db.collection("games")
                .document(currentGameId)
                .collection("comments")
                .document(currentUid)
                .set(comment)
                .addOnSuccessListener(unused -> {

                    isSendingComment = false;

                    commentInput.setText("");

                    sendCommentButton.setText("Опубликовать");
                    sendCommentButton.setEnabled(true);

                    Toast.makeText(
                            this,
                            "Комментарий опубликован!",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(error -> {

                    isSendingComment = false;

                    sendCommentButton.setText("Опубликовать");
                    sendCommentButton.setEnabled(true);

                    Toast.makeText(
                            this,
                            "Не удалось опубликовать комментарий. "
                                    + "Попробуй ещё раз.",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // ------------------------------------
    // ОТОБРАЖЕНИЕ КОММЕНТАРИЕВ
    // ------------------------------------

    private void renderComments(
            List<DocumentSnapshot> documents
    ) {

        commentsContainer.removeAllViews();

        int count = documents.size();

        commentsCount.setText(
                count + " " + getCommentWord(count)
        );

        if (count == 0) {

            TextView emptyText = new TextView(this);

            emptyText.setText(
                    "Пока никто не оставил комментарий. "
                            + "Будь первым!"
            );

            emptyText.setTextColor(Color.parseColor("#806B59"));
            emptyText.setTextSize(15);
            emptyText.setPadding(0, 18, 0, 18);

            commentsContainer.addView(emptyText);
            return;
        }

        for (DocumentSnapshot document : documents) {
            addCommentCard(document);
        }
    }

    private String getCommentWord(int count) {

        int lastTwo = count % 100;
        int lastOne = count % 10;

        if (lastTwo >= 11 && lastTwo <= 14) {
            return "комментариев";
        }

        if (lastOne == 1) {
            return "комментарий";
        }

        if (lastOne >= 2 && lastOne <= 4) {
            return "комментария";
        }

        return "комментариев";
    }

    private void addCommentCard(DocumentSnapshot document) {

        String username = document.getString("username");
        String avatarId = document.getString("avatarId");
        String text = document.getString("text");
        String userId = document.getString("userId");

        if (username == null) {
            username = "Пользователь";
        }

        if (text == null) {
            text = "";
        }

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(14, 14, 14, 14);

        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setColor(Color.parseColor("#FFF5E9"));
        cardBackground.setCornerRadius(dp(12));

        card.setBackground(cardBackground);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.bottomMargin = dp(12);

        card.setLayoutParams(cardParams);

        // Верхняя строка: аватар и никнейм
        LinearLayout header = new LinearLayout(this);

        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);

        ImageView avatar = new ImageView(this);

        LinearLayout.LayoutParams avatarParams =
                new LinearLayout.LayoutParams(
                        dp(44),
                        dp(44)
                );

        avatar.setLayoutParams(avatarParams);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int avatarResource = getResources().getIdentifier(
                avatarId == null ? "" : avatarId,
                "drawable",
                getPackageName()
        );

        if (avatarResource != 0) {
            avatar.setImageResource(avatarResource);
        } else {
            avatar.setImageResource(R.drawable.avatar1);
        }

        header.addView(avatar);

        LinearLayout userInfo = new LinearLayout(this);

        userInfo.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        infoParams.leftMargin = dp(10);

        userInfo.setLayoutParams(infoParams);

        TextView usernameText = new TextView(this);

        usernameText.setText(username);
        usernameText.setTextColor(Color.parseColor("#211812"));
        usernameText.setTextSize(15);
        usernameText.setTypeface(null, Typeface.BOLD);

        userInfo.addView(usernameText);

        TextView dateText = new TextView(this);

        dateText.setText(getCommentDate(document));
        dateText.setTextColor(Color.parseColor("#806B59"));
        dateText.setTextSize(12);

        userInfo.addView(dateText);

        header.addView(userInfo);

        // Удаление доступно только автору
        if (currentUid != null
                && currentUid.equals(userId)) {

            TextView deleteButton = new TextView(this);

            deleteButton.setText("Удалить");
            deleteButton.setTextColor(
                    Color.parseColor("#B84F13")
            );
            deleteButton.setTextSize(13);
            deleteButton.setPadding(
                    dp(8),
                    dp(8),
                    dp(4),
                    dp(8)
            );

            deleteButton.setOnClickListener(
                    v -> confirmDeleteComment(document.getId())
            );

            header.addView(deleteButton);
        }

        card.addView(header);

        // Текст комментария
        TextView commentText = new TextView(this);

        commentText.setText(text);
        commentText.setTextColor(Color.parseColor("#211812"));
        commentText.setTextSize(15);
        commentText.setLineSpacing(dp(3), 1f);

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        textParams.topMargin = dp(12);

        commentText.setLayoutParams(textParams);

        card.addView(commentText);

        commentsContainer.addView(card);
    }

    private String getCommentDate(DocumentSnapshot document) {

        Timestamp timestamp = document.getTimestamp("createdAt");

        if (timestamp == null) {
            return "Только что";
        }

        Date date = timestamp.toDate();

        SimpleDateFormat format = new SimpleDateFormat(
                "dd.MM.yyyy HH:mm",
                Locale.getDefault()
        );

        return format.format(date);
    }

    // ------------------------------------
    // УДАЛЕНИЕ СВОЕГО КОММЕНТАРИЯ
    // ------------------------------------

    private void confirmDeleteComment(String commentId) {

        new AlertDialog.Builder(this)
                .setTitle("Удалить комментарий?")
                .setMessage(
                        "Комментарий будет удалён без возможности восстановления."
                )
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Удалить", (dialog, which) ->
                        deleteComment(commentId)
                )
                .show();
    }

    private void deleteComment(String commentId) {

        if (currentUid == null
                || !currentUid.equals(commentId)) {
            return;
        }

        db.collection("games")
                .document(currentGameId)
                .collection("comments")
                .document(commentId)
                .delete()
                .addOnSuccessListener(unused -> {

                    Toast.makeText(
                            this,
                            "Комментарий удалён.",
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(error -> {

                    Toast.makeText(
                            this,
                            "Не удалось удалить комментарий.",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }

    private void showCommentError(String message) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    private int dp(int value) {

        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    // ------------------------------------
    // ИНФОРМАЦИЯ ОБ ИГРЕ
    // ------------------------------------

    private void displayMetadata(GameDetailsData game) {

        StringBuilder metadata = new StringBuilder();

        addMetadataLine(metadata, "Жанр", game.genre);
        addMetadataLine(metadata, "Разработчик", game.developer);
        addMetadataLine(metadata, "Дата выхода", game.releaseDate);
        addMetadataLine(metadata, "Платформы", game.platforms);

        if (metadata.length() > 0) {
            metadataText.setText(metadata.toString());
            metadataText.setVisibility(View.VISIBLE);
        } else {
            metadataText.setVisibility(View.GONE);
        }
    }

    private void addMetadataLine(
            StringBuilder builder,
            String label,
            String value
    ) {
        if (value != null && !value.trim().isEmpty()) {

            if (builder.length() > 0) {
                builder.append("\n");
            }

            builder.append(label)
                    .append(": ")
                    .append(value);
        }
    }

    private void displayDescription(GameDetailsData game) {

        if (game.description != null
                && !game.description.trim().isEmpty()) {

            descriptionText.setText(game.description);
            descriptionText.setVisibility(View.VISIBLE);

        } else {
            descriptionText.setVisibility(View.GONE);
        }
    }

    private void displayStoreButton(GameDetailsData game) {

        Button storeButton =
                findViewById(R.id.gameDetailsStoreButton);

        if (game.storeUrl != null
                && !game.storeUrl.trim().isEmpty()) {

            storeButton.setTag(game.storeUrl);
            storeButton.setVisibility(View.VISIBLE);

        } else {
            storeButton.setVisibility(View.GONE);
        }
    }

    private void displayRequirements(GameDetailsData game) {

        if (!game.hasPcVersion
                || game.minimumRequirements == null
                || game.minimumRequirements.trim().isEmpty()
                || game.recommendedRequirements == null
                || game.recommendedRequirements.trim().isEmpty()) {

            requirementsLayout.setVisibility(View.GONE);
            return;
        }

        minimumRequirements = game.minimumRequirements;
        recommendedRequirements = game.recommendedRequirements;

        requirementsContent.setText(minimumRequirements);

        requirementsLayout.setVisibility(View.VISIBLE);
    }

    private void displayGallery(GameDetailsData game) {

        screenshots = game.screenshots;

        if (screenshots == null || screenshots.isEmpty()) {
            galleryLayout.setVisibility(View.GONE);
            return;
        }

        currentScreenshot = 0;

        galleryLayout.setVisibility(View.VISIBLE);

        showScreenshot();
    }

    private void showScreenshot() {

        if (screenshots == null || screenshots.isEmpty()) {
            return;
        }

        galleryImage.setImageResource(
                screenshots.get(currentScreenshot)
        );

        galleryCounter.setText(
                (currentScreenshot + 1) + " / " + screenshots.size()
        );
    }

    private void showNextScreenshot() {

        if (screenshots == null || screenshots.isEmpty()) {
            return;
        }

        currentScreenshot++;

        if (currentScreenshot >= screenshots.size()) {
            currentScreenshot = 0;
        }

        showScreenshot();
    }

    private void showPreviousScreenshot() {

        if (screenshots == null || screenshots.isEmpty()) {
            return;
        }

        currentScreenshot--;

        if (currentScreenshot < 0) {
            currentScreenshot = screenshots.size() - 1;
        }

        showScreenshot();
    }

    private void setupGallerySwipe() {

        GestureDetector detector = new GestureDetector(
                this,
                new GestureDetector.SimpleOnGestureListener() {

                    private static final int SWIPE_DISTANCE = 80;
                    private static final int SWIPE_VELOCITY = 80;

                    @Override
                    public boolean onFling(
                            MotionEvent e1,
                            MotionEvent e2,
                            float velocityX,
                            float velocityY
                    ) {

                        if (e1 == null || e2 == null) {
                            return false;
                        }

                        float distanceX = e2.getX() - e1.getX();
                        float distanceY = e2.getY() - e1.getY();

                        if (Math.abs(distanceX) > Math.abs(distanceY)
                                && Math.abs(distanceX) > SWIPE_DISTANCE
                                && Math.abs(velocityX) > SWIPE_VELOCITY) {

                            if (distanceX < 0) {
                                showNextScreenshot();
                            } else {
                                showPreviousScreenshot();
                            }

                            return true;
                        }

                        return false;
                    }
                }
        );

        galleryImage.setOnTouchListener((view, event) -> {
            detector.onTouchEvent(event);
            return true;
        });
    }
}
