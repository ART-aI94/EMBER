
package com.example.ember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideDetailActivity extends AppCompatActivity {

    public static final String EXTRA_GAME = "guide_game";

    private final int textColor = 0xFF211812;
    private final int secondaryColor = 0xFF806F60;
    private final int accentColor = 0xFFB84F13;

    private LinearLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);

        contentContainer = findViewById(R.id.guideContent);

        setupBottomNavigation();

        String game = getIntent().getStringExtra(EXTRA_GAME);

        if ("elden".equals(game)) {
            showEldenRingGuide();
        } else if ("sekiro".equals(game)) {
            showSekiroGuide();
        }
    }

    // ==========================================
    // НИЖНЯЯ НАВИГАЦИЯ
    // ==========================================

    private void setupBottomNavigation() {

        findViewById(R.id.navHome).setOnClickListener(v ->
                navigateTo(HomeActivity.class)
        );

        findViewById(R.id.navSearch).setOnClickListener(v ->
                navigateTo(SearchActivity.class)
        );

        findViewById(R.id.navNews).setOnClickListener(v ->
                navigateTo(NewsActivity.class)
        );

        findViewById(R.id.navGuides).setOnClickListener(v ->
                navigateTo(GuidesActivity.class)
        );
    }

    private void navigateTo(Class<?> destination) {

        Intent intent = new Intent(
                GuideDetailActivity.this,
                destination
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);
        finish();
    }

    // ==========================================
    // ELDEN RING
    // ==========================================

    private void showEldenRingGuide() {

        setHeader(
                "ELDEN RING",
                "Все боссы Междуземья",
                "Подробный справочник по боссам Междуземья: "
                        + "местонахождение, особенности сражений "
                        + "и награды за победу."
        );

        addSection(
                "Введение. Мир, полный испытаний",
                "Междуземье — огромный мир, в котором почти за каждым "
                        + "поворотом может скрываться противник, способный "
                        + "стать серьёзным испытанием даже для опытного Погасшего. "
                        + "Здесь нет единственного правильного маршрута: "
                        + "игрок сам выбирает, какие земли исследовать, "
                        + "какие тайны раскрывать и с кем вступать в бой."
        );

        addParagraph(
                "Боссы в Elden Ring отличаются не только силой, "
                        + "но и своим значением для прохождения. "
                        + "Одни связаны с основным сюжетом, другие охраняют "
                        + "подземелья и сокровища, а третьи встречаются "
                        + "во время свободного исследования открытого мира."
        );

        addSection(
                "Какие бывают боссы",
                "Сюжетные боссы — противники, связанные с продвижением "
                        + "по основному пути игры.\n\n"
                        + "Великие боссы — важнейшие фигуры истории Междуземья, "
                        + "некоторые из которых обладают Великими рунами.\n\n"
                        + "Дополнительные боссы — необязательные противники "
                        + "в пещерах, катакомбах, крепостях и других местах.\n\n"
                        + "Полевые боссы — сильные враги, встречающиеся "
                        + "непосредственно в открытом мире."
        );

        addSection(
                "01. Лимгрейв — начало путешествия",
                "Лимгрейв — первая большая область, которую исследует "
                        + "Погасший после выхода из начальной пещеры. "
                        + "Зелёные равнины, руины, небольшие поселения "
                        + "и древние подземелья создают обманчивое "
                        + "впечатление спокойствия.\n\n"
                        + "Именно здесь игрок знакомится с основными "
                        + "принципами боевой системы. Некоторые противники "
                        + "могут показаться простыми, но встречаются и те, "
                        + "с кем лучше повременить до получения нового "
                        + "снаряжения и нескольких уровней."
        );

        addBoss(
                "Маргит, Ужасное Знамение",
                "Margit, the Fell Omen",
                "Подход к Замку Грозовой Завесы.",
                "Маргит становится первым серьёзным испытанием "
                        + "для многих игроков. Он использует посох, "
                        + "призрачные клинки и золотое оружие, "
                        + "которое появляется прямо во время атаки.\n\n"
                        + "Его движения намеренно сбивают с толку: "
                        + "некоторые удары имеют задержку, из-за чего "
                        + "слишком раннее уклонение приводит к получению урона. "
                        + "Лучше дождаться окончания серии ударов.",
                "Талисманная сумка и руны."
        );

        addBoss(
                "Деревянный страж",
                "Tree Sentinel",
                "Дорога неподалёку от Первого шага.",
                "Этот тяжеловооружённый всадник встречает игрока "
                        + "практически сразу после выхода в открытый мир. "
                        + "Его золотая алебарда наносит большой урон, "
                        + "а щит позволяет отражать некоторые атаки.\n\n"
                        + "Сражение необязательно начинать сразу. "
                        + "Гораздо разумнее вернуться позже, когда "
                        + "персонаж станет сильнее.",
                "Золотая алебарда и руны."
        );

        addBoss(
                "Летающий дракон Эгхил",
                "Flying Dragon Agheel",
                "Озеро Эгхил.",
                "Один из первых драконов, встречающихся в открытом мире. "
                        + "Эгхил атакует огнём, взмахами крыльев "
                        + "и мощными ударами.\n\n"
                        + "Сражение удобно проводить верхом, используя "
                        + "Торрента для быстрого перемещения. "
                        + "Старайтесь не задерживаться перед его головой "
                        + "и следите за моментом, когда дракон поднимается в воздух.",
                "Драконье сердце и руны."
        );

        addParagraph(
                "Это только начало путешествия. В Лимгрейве "
                        + "и соседних областях скрывается ещё множество "
                        + "противников, которых предстоит обнаружить."
        );
    }

    // ==========================================
    // SEKIRO
    // ==========================================

    private void showSekiroGuide() {

        setHeader(
                "SEKIRO: SHADOWS DIE TWICE",
                "Советы для новичков",
                "Sekiro — игра, в которой победа зависит не столько "
                        + "от уровня персонажа, сколько от реакции, "
                        + "точности и понимания противника. "
                        + "Это руководство поможет освоить основные "
                        + "механики и подготовиться к сложным сражениям."
        );

        addSection(
                "01. Главное правило — не бойся атаковать",
                "В Sekiro боевая система построена вокруг постоянного "
                        + "противостояния. В отличие от многих других игр "
                        + "FromSoftware, здесь недостаточно просто "
                        + "уклоняться от каждого удара и ждать удобного момента.\n\n"
                        + "Тебе придётся одновременно атаковать противника, "
                        + "отражать его удары и следить за состоянием его стойки. "
                        + "Если постоянно отступать, враг сможет восстановить "
                        + "свою стойку, и сражение затянется.\n\n"
                        + "Не бойся вступать в ближний бой. Во многих случаях "
                        + "агрессивная игра помогает быстрее победить."
        );

        addSection(
                "02. Парирование — основа боевой системы",
                "Самая важная механика Sekiro — отражение атак. "
                        + "Для этого необходимо нажать кнопку защиты "
                        + "непосредственно перед попаданием удара противника.\n\n"
                        + "Успешное отражение сопровождается характерным "
                        + "звуком и визуальным эффектом. Оно позволяет "
                        + "не только избежать урона, но и увеличить "
                        + "стойку противника.\n\n"
                        + "Обычная защита тоже полезна, особенно в начале "
                        + "прохождения. Однако постоянное удерживание блока "
                        + "не заменяет своевременного парирования.\n\n"
                        + "Лучше тренироваться на обычных противниках: "
                        + "постепенно ты начнёшь узнавать ритм атак "
                        + "и перестанешь нажимать защиту наугад."
        );

        addSubheading("Как улучшить парирование");

        addParagraph(
                "Не зажимай кнопку защиты без необходимости. "
                        + "Старайся нажимать её в момент приближения удара.\n\n"
                        + "Обращай внимание на звук столкновения клинков. "
                        + "Он помогает понять, получилось ли отражение.\n\n"
                        + "Не пытайся парировать всё подряд с первой попытки. "
                        + "Изучай движения конкретного противника "
                        + "и запоминай последовательность его атак."
        );

        addSection(
                "03. Стойка противника и смертельный удар",
                "В Sekiro у каждого бойца есть шкала стойки. "
                        + "Она постепенно заполняется, когда ты успешно "
                        + "атакуешь или отражаешь его удары.\n\n"
                        + "Когда стойка противника полностью сломана, "
                        + "появляется возможность выполнить смертельный удар. "
                        + "Именно таким способом часто заканчиваются "
                        + "сражения с сильными врагами и боссами.\n\n"
                        + "Важно понимать, что обычный урон и урон по стойке "
                        + "связаны. Если у противника остаётся много здоровья, "
                        + "его стойка восстанавливается быстрее. Поэтому "
                        + "иногда сначала необходимо нанести несколько "
                        + "обычных ударов, а уже затем сосредоточиться "
                        + "на разрушении стойки."
        );

        addSection(
                "04. Опасные атаки: учись читать противника",
                "Некоторые атаки в Sekiro нельзя просто заблокировать "
                        + "обычным способом. Перед ними появляется "
                        + "специальный красный символ. Такие движения "
                        + "называются опасными атаками.\n\n"
                        + "У них есть несколько основных разновидностей. "
                        + "Для каждой требуется своя реакция."
        );

        addSubheading("Колющие атаки");

        addParagraph(
                "Против колющих атак помогает Микири — специальная "
                        + "контратака, которая позволяет наступить на оружие "
                        + "противника и значительно увеличить его стойку.\n\n"
                        + "Для выполнения нужно правильно выбрать момент "
                        + "и нажать кнопку уклонения навстречу атаке. "
                        + "Этому навыку стоит научиться как можно раньше."
        );

        addSubheading("Размашистые атаки");

        addParagraph(
                "Если противник проводит низкую размашистую атаку, "
                        + "обычный блок не поможет. Необходимо прыгнуть "
                        + "и избежать удара. Иногда можно дополнительно "
                        + "прыгнуть на противника, чтобы нанести урон его стойке."
        );

        addSubheading("Захваты");

        addParagraph(
                "Против захватов обычно необходимо вовремя уклониться "
                        + "и выйти из опасной зоны. Не пытайся отражать "
                        + "такую атаку обычной защитой."
        );

        addSection(
                "05. Протез синоби — больше, чем дополнительное оружие",
                "Протез синоби позволяет использовать специальные "
                        + "инструменты, которые помогают справляться "
                        + "с разными типами противников.\n\n"
                        + "Каждый инструмент имеет своё назначение. "
                        + "Одни позволяют прерывать атаки, другие помогают "
                        + "наносить дополнительный урон или создавать "
                        + "удобные условия для нападения.\n\n"
                        + "Для большинства инструментов необходимы "
                        + "эмблемы духа. Их количество ограничено, "
                        + "поэтому не стоит тратить их бездумно."
        );

        addSubheading("Основные инструменты");

        addParagraph(
                "Сюрикены — полезны против противников, которые "
                        + "пытаются держаться на расстоянии или находятся "
                        + "в воздухе.\n\n"
                        + "Петарды — позволяют оглушать многих противников "
                        + "и создавать короткое окно для атаки. Особенно "
                        + "полезны против некоторых зверей.\n\n"
                        + "Топор синоби — помогает разрушать щиты "
                        + "и деревянную защиту.\n\n"
                        + "Огненный ствол — наносит огненный урон "
                        + "и может помочь против врагов, уязвимых к огню.\n\n"
                        + "Зонтик — позволяет защищаться от ряда опасных "
                        + "атак и облегчает некоторые сложные сражения."
        );

        addSection(
                "06. Скрытность и исследование мира",
                "Не каждое сражение в Sekiro необходимо начинать "
                        + "в открытом бою. Главный герой — синоби, "
                        + "поэтому скрытность является важной частью игры.\n\n"
                        + "Используй высокие точки, крыши и траву, "
                        + "чтобы обходить противников и изучать их расположение. "
                        + "Многие обычные враги могут быть устранены "
                        + "скрытным смертельным ударом.\n\n"
                        + "Перед нападением на сильного противника "
                        + "полезно осмотреть территорию. Иногда можно "
                        + "сначала убрать его помощников, чтобы не "
                        + "сражаться сразу с несколькими врагами."
        );

        addSection(
                "07. Лечение и воскрешение",
                "Основное средство восстановления здоровья — "
                        + "тыква с лечебным отваром. Количество "
                        + "использований ограничено, но его можно "
                        + "увеличивать по мере прохождения.\n\n"
                        + "Не трать лечение после каждого небольшого "
                        + "полученного удара. Лучше использовать его "
                        + "в безопасный момент, когда противник "
                        + "завершил свою комбинацию или находится далеко.\n\n"
                        + "Механика воскрешения позволяет вернуться "
                        + "в бой после смертельного удара, если "
                        + "доступна соответствующая возможность. "
                        + "Однако повторные настоящие смерти могут "
                        + "способствовать распространению Драконьего "
                        + "поветрия среди персонажей мира."
        );

        addSection(
                "08. Как правильно сражаться с боссами",
                "Не пытайся победить каждого босса с первой попытки. "
                        + "В Sekiro поражение часто становится способом "
                        + "изучить противника.\n\n"
                        + "Во время первой попытки сосредоточься "
                        + "на наблюдении. Посмотри, какие атаки "
                        + "он использует, как долго продолжаются "
                        + "комбинации и какие движения можно отразить.\n\n"
                        + "Затем постепенно переходи к активной игре. "
                        + "Атакуй, когда есть возможность, отражай "
                        + "удары и не позволяй противнику спокойно "
                        + "восстанавливать стойку.\n\n"
                        + "Некоторые боссы имеют несколько фаз. "
                        + "После победы над первой фазой не расслабляйся: "
                        + "следующая может полностью изменить привычный "
                        + "ритм боя."
        );

        addSection(
                "09. Ошибки, которых стоит избегать",
                "Постоянное уклонение. В Sekiro уклонение не является "
                        + "универсальным ответом на каждую атаку. "
                        + "Чаще всего правильное отражение гораздо полезнее.\n\n"
                        + "Бездумное удерживание блока. Защита помогает, "
                        + "но не заменяет изучение таймингов парирования.\n\n"
                        + "Игнорирование опасных атак. Красный символ "
                        + "означает, что нужно определить тип атаки "
                        + "и правильно на неё отреагировать.\n\n"
                        + "Отказ от скрытности. Иногда можно значительно "
                        + "облегчить сражение, устранив часть врагов "
                        + "до начала открытого боя.\n\n"
                        + "Беспорядочное использование протеза. "
                        + "Эмблемы духа ограничены, поэтому инструменты "
                        + "лучше применять в подходящие моменты."
        );

        addSection(
                "Заключение",
                "Sekiro требует терпения и внимания. Здесь нельзя "
                        + "полностью полагаться на прокачку или "
                        + "постоянное уклонение от атак.\n\n"
                        + "Главный ключ к победе — научиться "
                        + "чувствовать ритм боя. Отражай удары, "
                        + "разрушай стойку противников, используй "
                        + "протез синоби и не забывай о скрытности.\n\n"
                        + "Каждое поражение даёт возможность узнать "
                        + "что-то новое. Со временем движения врагов "
                        + "станут понятнее, а сложные сражения "
                        + "перестанут казаться невозможными."
        );
    }

    // ==========================================
    // ОБЩИЕ ЭЛЕМЕНТЫ ОФОРМЛЕНИЯ
    // ==========================================

    private void setHeader(String game, String title, String intro) {

        TextView gameLabel = new TextView(this);
        gameLabel.setText(game);
        gameLabel.setTextColor(accentColor);
        gameLabel.setTextSize(12);
        gameLabel.setLetterSpacing(0.12f);
        gameLabel.setTypeface(null, android.graphics.Typeface.BOLD);

        contentContainer.addView(gameLabel);

        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(textColor);
        titleView.setTextSize(30);
        titleView.setTypeface(
                android.graphics.Typeface.SERIF,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.topMargin = dp(8);
        contentContainer.addView(titleView, titleParams);

        TextView introView = new TextView(this);
        introView.setText(intro);
        introView.setTextColor(secondaryColor);
        introView.setTextSize(15);
        introView.setLineSpacing(dp(4), 1f);

        LinearLayout.LayoutParams introParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        introParams.topMargin = dp(10);
        contentContainer.addView(introView, introParams);

        addDivider(22);
    }

    private void addSection(String title, String text) {

        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextColor(accentColor);
        heading.setTextSize(21);
        heading.setTypeface(
                android.graphics.Typeface.SERIF,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams headingParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        headingParams.topMargin = dp(20);
        headingParams.bottomMargin = dp(10);

        contentContainer.addView(heading, headingParams);

        addParagraph(text);
    }

    private void addBoss(
            String name,
            String english,
            String location,
            String features,
            String reward
    ) {

        TextView nameView = new TextView(this);
        nameView.setText(name);
        nameView.setTextColor(textColor);
        nameView.setTextSize(19);
        nameView.setTypeface(
                android.graphics.Typeface.SERIF,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        nameParams.topMargin = dp(20);
        contentContainer.addView(nameView, nameParams);

        TextView englishView = new TextView(this);
        englishView.setText(english);
        englishView.setTextColor(secondaryColor);
        englishView.setTextSize(13);

        LinearLayout.LayoutParams englishParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        englishParams.topMargin = dp(3);
        englishParams.bottomMargin = dp(12);

        contentContainer.addView(englishView, englishParams);

        addSubheading("Местонахождение");
        addParagraph(location);

        addSubheading("Особенности сражения");
        addParagraph(features);

        addSubheading("Награда");
        addParagraph(reward);

        addDivider(14);
    }

    private void addSubheading(String text) {

        TextView view = new TextView(this);
        view.setText(text.toUpperCase());
        view.setTextColor(accentColor);
        view.setTextSize(12);
        view.setTypeface(null, android.graphics.Typeface.BOLD);
        view.setLetterSpacing(0.06f);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.topMargin = dp(12);
        params.bottomMargin = dp(4);

        contentContainer.addView(view, params);
    }

    private void addParagraph(String text) {

        TextView paragraph = new TextView(this);
        paragraph.setText(text);
        paragraph.setTextColor(textColor);
        paragraph.setTextSize(15);
        paragraph.setLineSpacing(dp(5), 1f);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.bottomMargin = dp(10);

        contentContainer.addView(paragraph, params);
    }

    private void addDivider(int topMargin) {

        View divider = new View(this);
        divider.setBackgroundColor(0xFFD8BFA5);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(1)
                );

        params.topMargin = dp(topMargin);
        params.bottomMargin = dp(18);

        contentContainer.addView(divider, params);
    }

    private int dp(int value) {
        return (int) (
                value * getResources().getDisplayMetrics().density
        );
    }
}
