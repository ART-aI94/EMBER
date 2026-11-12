
package com.example.ember;

public class GameDetailsRepository {

    public static GameDetailsData getGame(String title) {
        if (title == null) return null;

        switch (title) {
            case "Armored Core":
                return game(title, "Экшен, симулятор мехов", "FromSoftware",
                        "1997", "PlayStation",
                        "Пилотируйте боевого меха, выполняйте контракты и сражайтесь с противниками. Настраивайте вооружение, броню и оборудование под каждую миссию.",
                        "");

            case "Armored Core 2":
                return game(title, "Экшен, симулятор мехов", "FromSoftware",
                        "2000", "PlayStation 2",
                        "Управляйте боевым роботом в мире будущего. Выполняйте задания, зарабатывайте средства и совершенствуйте машину, подбирая детали и вооружение.",
                        "");

            case "Armored Core 3":
                return game(title, "Экшен, симулятор мехов", "FromSoftware",
                        "2002", "PlayStation 2",
                        "Сражайтесь в мире, где корпорации ведут борьбу за власть. Собирайте собственного меха, выбирайте оружие и выполняйте боевые контракты.",
                        "");

            case "Armored Core 4":
                return game(title, "Экшен, симулятор мехов", "FromSoftware",
                        "2006 (Япония)", "PlayStation 3, Xbox 360",
                        "Высокоскоростные сражения боевых мехов. Создавайте собственную конфигурацию машины, используйте современное вооружение и участвуйте в конфликтах крупных корпораций.",
                        "");

            case "Bloodborne":
                return game(title, "Action RPG, Souls-like", "FromSoftware",
                        "2015", "PlayStation 4",
                        "Исследуйте мрачный город Ярнам, охваченный загадочной болезнью. В роли Охотника сражайтесь с чудовищами, раскрывайте тайны города и переживите ночь, полную кошмаров.\n\nБыстрые бои, трансформируемое оружие и готическая атмосфера делают каждое столкновение опасным.",
                        "https://www.playstation.com/en-us/games/bloodborne/");

            case "Dark Souls":
                return darkSouls();

            case "Dark Souls 2":
                return darkSouls2();

            case "Dark Souls 3":
                return game(title, "Action RPG, Souls-like", "FromSoftware",
                        "2016", "PC, PlayStation 4, Xbox One",
                        "Путешествуйте по угасающему миру, где сходятся земли древних королевств. Сражайтесь с опасными противниками, побеждайте боссов и собирайте фрагменты истории через окружение и описания предметов.\n\nРазвивайте персонажа, экспериментируйте с оружием и магией и ищите собственный стиль боя.",
                        "https://store.steampowered.com/app/374320/");

            case "Echo Night":
                return game(title, "Приключение, хоррор", "FromSoftware",
                        "1998", "PlayStation",
                        "Исследуйте таинственные места, взаимодействуйте с духами и раскрывайте истории людей, связанных с трагическими событиями. Здесь важны наблюдательность, исследование и решение загадок, а не только сражения.",
                        "");

            case "Echo Night: Beyond":
                return game(title, "Приключение, психологический хоррор", "FromSoftware",
                        "2004", "PlayStation 2",
                        "После катастрофы на Луне герой оказывается в заброшенном комплексе, наполненном призраками. Исследуйте помещения, находите предметы и помогайте душам завершить незаконченные дела.",
                        "");

            case "Elden Ring":
                return eldenRing();

            case "Elden Ring: Nightreign":
                return game(title, "Кооперативный экшен, roguelike", "FromSoftware",
                        "2025", "PC, PlayStation 4, PlayStation 5, Xbox One, Xbox Series X|S",
                        "Отправляйтесь в опасные экспедиции по изменяющемуся миру. Выбирайте героя с уникальными способностями, объединяйтесь с другими игроками и сражайтесь с могущественными противниками, пока над землёй сгущается ночь.",
                        "https://store.steampowered.com/app/2622380/");

            case "Elden Ring: Shadow of the Erdtree":
                return game(title, "Дополнение, Action RPG", "FromSoftware",
                        "2024", "PC, PlayStation 4, PlayStation 5, Xbox One, Xbox Series X|S",
                        "Дополнение к Elden Ring переносит игрока в Тень Земли Древа. Исследуйте новые области, открывайте подземелья, находите оружие и заклинания и сражайтесь с новыми боссами.\n\nДля игры требуется оригинальная Elden Ring.",
                        "https://store.steampowered.com/app/2778580/");

            case "King's Field":
                return game(title, "Ролевая игра, dungeon crawler", "FromSoftware",
                        "1994 (Япония)", "PlayStation",
                        "Исследуйте подземные лабиринты от первого лица, сражайтесь с чудовищами, находите сокровища и постепенно раскрывайте тайны древнего мира.",
                        "");

            case "King's Field 2":
                return game(title, "Ролевая игра, dungeon crawler", "FromSoftware",
                        "1995 (Япония)", "PlayStation",
                        "Отправляйтесь на опасный остров, полный подземелий, противников и древних тайн. Исследуйте мир от первого лица, улучшайте снаряжение и развивайте героя.",
                        "");

            case "King's Field 3":
                return game(title, "Ролевая игра, dungeon crawler", "FromSoftware",
                        "1996 (Япония)", "PlayStation",
                        "Продолжение серии King's Field с исследованием большого фэнтезийного мира от первого лица. Изучайте подземелья, находите магические предметы и сражайтесь с опасными существами.",
                        "");

            case "King's Field 4":
                return game(title, "Ролевая игра, dungeon crawler", "FromSoftware",
                        "2001", "PlayStation 2",
                        "Исследуйте древнюю землю, поражённую проклятием. Путешествуйте по мрачным руинам, сражайтесь с монстрами и находите предметы, необходимые для продвижения.",
                        "");

            case "Kuon":
                return game(title, "Хоррор, приключение", "FromSoftware",
                        "2004", "PlayStation 2",
                        "Мистический хоррор в декорациях древней Японии. Исследуйте проклятое поместье, сталкивайтесь с потусторонними существами и раскрывайте тайну ритуалов, скрытых в его стенах.",
                        "");

            case "Lost Kingdoms":
                return game(title, "Action RPG, карточная механика", "FromSoftware",
                        "2002", "Nintendo GameCube",
                        "Используйте магические карты, чтобы призывать существ и сражаться с противниками. Собирайте новые карты, подбирайте тактику и исследуйте фэнтезийный мир, охваченный загадочной угрозой.",
                        "");

            case "Shadow Tower":
                return game(title, "Action RPG, dungeon crawler", "FromSoftware",
                        "1998", "PlayStation",
                        "Спускайтесь в мрачную башню, населённую опасными существами. Исследуйте запутанные уровни, управляйте запасами снаряжения и ищите путь через суровое подземелье.",
                        "");

            case "Shadow Tower: Abyss":
                return game(title, "Action RPG, dungeon crawler", "FromSoftware",
                        "2003 (Япония)", "PlayStation 2",
                        "Исследуйте огромные подземные пространства, сражайтесь с чудовищами и собирайте экипировку. Продолжение Shadow Tower делает акцент на выживании, исследовании и опасных столкновениях.",
                        "");

            case "Tenchu: Fatal Shadows":
                return game(title, "Стелс-экшен", "K2 LLC",
                        "2004", "PlayStation 2",
                        "Станьте ниндзя в феодальной Японии. Пробирайтесь мимо охраны, используйте тени и окружение, выполняйте скрытные устранения и выбирайте собственный путь прохождения миссий.",
                        "");

            case "Tenchu: Shadow Assassins":
                return game(title, "Стелс-экшен", "Acquire",
                        "2008", "Wii, PSP",
                        "Вернитесь в мир ниндзя и скрытных операций. Используйте окружение, отвлекайте противников и выполняйте задания, стараясь оставаться незамеченным.",
                        "");

            case "Tenchu: Time of the Assassins":
                return game(title, "Стелс-экшен", "FromSoftware",
                        "2005 (Япония)", "PlayStation Portable",
                        "Выполняйте миссии в роли ниндзя, проникайте на охраняемые территории и устраняйте врагов скрытно. Игра объединяет персонажей и задания из разных частей серии Tenchu.",
                        "");

            case "Tenchu: Wrath of Heaven":
                return game(title, "Стелс-экшен", "K2 LLC",
                        "2003", "PlayStation 2, Xbox",
                        "Играйте за ниндзя Рикимару или Аямэ и выполняйте опасные задания. Прячьтесь в тени, используйте ловушки и бесшумные атаки, чтобы пройти миссии с минимальным риском обнаружения.",
                        "");

            case "The Duskbloods":
                return game(title, "Многопользовательский экшен", "FromSoftware",
                        "2026", "Nintendo Switch 2",
                        "Мрачный экшен FromSoftware, созданный для Nintendo Switch 2. Игра сосредоточена на противостоянии персонажей с особыми способностями в готическом мире.\n\nПеред публикацией точные сведения о режимах и дате релиза следует сверить с актуальной официальной информацией.",
                        "");

            case "Sekiro: Shadows Die Twice":
                return sekiro();

            default:
                return null;
        }
    }

    private static GameDetailsData game(
            String title,
            String genre,
            String developer,
            String releaseDate,
            String platforms,
            String description,
            String storeUrl
    ) {
        return new GameDetailsData(
                title, genre, developer, releaseDate,
                platforms, description, storeUrl
        );
    }


    public static String getGameId(String title) {
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
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");
    }


    private static GameDetailsData darkSouls() {
        GameDetailsData g = game(
                "Dark Souls",
                "Action RPG, Souls-like",
                "FromSoftware",
                "2011",
                "PC, PlayStation 3, Xbox 360",
                "Отправляйтесь в Лордран — мрачный мир, где угасает Эпоха Огня. Исследуйте древние крепости, забытые города и опасные подземелья.\n\nСражайтесь с могущественными боссами, изучайте поведение врагов и развивайте персонажа. Dark Souls известна сложными боями и взаимосвязанным миром.",
                "https://store.steampowered.com/app/570940/"
        );

        g.setPcRequirements(
                "ОС: Windows 7 SP1\nПроцессор: Intel Core 2 Duo E6850\nОЗУ: 3 ГБ\nВидеокарта: NVIDIA GeForce 9800 GTX+\nDirectX: 10\nМесто на диске: 8 ГБ",
                "Для точных рекомендованных требований проверьте страницу выбранного издания в Steam."
        );

        return g;
    }

    private static GameDetailsData darkSouls2() {
        GameDetailsData g = game(
                "Dark Souls 2",
                "Action RPG, Souls-like",
                "FromSoftware",
                "2014",
                "PC, PlayStation 3, Xbox 360, PlayStation 4, Xbox One",
                "Действие происходит в королевстве Дранглик, где герой ищет способ избавиться от проклятия нежити.\n\nИсследуйте руины и подземелья, сражайтесь с чудовищами и боссами, собирайте души и развивайте персонажа.",
                "https://store.steampowered.com/app/236430/"
        );

        g.setPcRequirements(
                "ОС: Windows Vista SP2 / Windows 7 SP1 / Windows 8\nПроцессор: Intel Core 2 Duo E8500 или AMD Phenom II X2 555\nОЗУ: 2 ГБ\nВидеокарта: NVIDIA GeForce 9600 GT или ATI Radeon HD 5870\nDirectX: 9.0c\nМесто на диске: 12 ГБ",
                "ОС: Windows 7 SP1 / Windows 8\nПроцессор: Intel Core i3-2100 или AMD A8-3870K\nОЗУ: 4 ГБ\nВидеокарта: NVIDIA GeForce GTX 750 или ATI Radeon HD 6870\nDirectX: 9.0c\nМесто на диске: 15 ГБ"
        );

        return g;
    }

    private static GameDetailsData eldenRing() {
        GameDetailsData g = game(
                "Elden Ring",
                "Action RPG, Souls-like, открытый мир",
                "FromSoftware",
                "2022",
                "PC, PlayStation 4, PlayStation 5, Xbox One, Xbox Series X|S",
                "Исследуйте Междуземье — огромный фэнтезийный мир, полный руин, подземелий и могущественных противников. Собирайте осколки Кольца Элден и определите судьбу мира.\n\nСоздавайте собственного героя, выбирайте оружие, магию и стиль боя. Исследуйте мир пешком или верхом и открывайте необязательные испытания.",
                "https://store.steampowered.com/app/1245620/"
        );

        g.setPcRequirements(
                "ОС: Windows 10\nПроцессор: Intel Core i5-8400 или AMD Ryzen 3 3300X\nОЗУ: 12 ГБ\nВидеокарта: NVIDIA GTX 1060 3 ГБ или AMD Radeon RX 580 4 ГБ\nDirectX: 12\nМесто на диске: 60 ГБ",
                "ОС: Windows 10/11\nПроцессор: Intel Core i7-8700K или AMD Ryzen 5 3600X\nОЗУ: 16 ГБ\nВидеокарта: NVIDIA GTX 1070 8 ГБ или AMD Radeon RX Vega 56 8 ГБ\nDirectX: 12\nМесто на диске: 60 ГБ"
        );

        return g;
    }

    private static GameDetailsData sekiro() {
        GameDetailsData g = game(
                "Sekiro: Shadows Die Twice",
                "Приключенческий экшен",
                "FromSoftware",
                "2019",
                "PC, PlayStation 4, Xbox One",
                "Япония эпохи Сэнгоку. Однорукий Волк отправляется спасать своего господина и возвращать утраченную честь.\n\nОсваивайте отражение атак, владение катаной и инструменты протеза. Используйте скрытность и вертикальное перемещение, чтобы побеждать опасных противников.",
                "https://store.steampowered.com/app/814380/"
        );

        g.setPcRequirements(
                "ОС: Windows 7/8/10, 64-bit\nПроцессор: Intel Core i3-2100 или AMD FX-6300\nОЗУ: 4 ГБ\nВидеокарта: NVIDIA GTX 760 или AMD Radeon HD 7950\nDirectX: 11\nМесто на диске: 25 ГБ",
                "ОС: Windows 7/8/10, 64-bit\nПроцессор: Intel Core i5-2500K или AMD Ryzen 5 1400\nОЗУ: 8 ГБ\nВидеокарта: NVIDIA GTX 970 или AMD Radeon RX 570\nDirectX: 11\nМесто на диске: 25 ГБ"
        );

        return g;
    }
}
