package com.gmail.uia059466.liska.data.database

class AppDatabaseInitializer {
  companion object {
    
    fun requestRussianData() =
            listOf(
                    
                    CatalogDatabase(
                            id = 1,
                            title = "Холодильник-запас",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "молоко сгущенное",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "яйца 1 десяток",
                                            quantity = 1,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "томатная паста 200г",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "молоко 2,5",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сметана 20%",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "картофель",
                                            quantity = 3,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "морковь",
                                            quantity = 1,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "лук",
                                            quantity = 1,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "чеснок",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "капуста",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "фасоль в томатном соусе",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сайра в масле",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "масло сливочное",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 2,
                            title = "Морозилка - запас",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "окорочка",
                                            quantity = 8,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "куриные бедра",
                                            quantity = 8,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "свинина",
                                            quantity = 3,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "фарш из окорочков",
                                            quantity = 2,
                                            unit = "кг",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "ребра говядина",
                                            quantity = 1,
                                            unit = "кг",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "форель",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "свинина на кости",
                                            quantity = 6,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 3,
                            title = "Бакалея - запас",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "удон 200г",
                                            quantity = 5,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "макароны",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "рис",
                                            quantity = 3,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "гречка",
                                            quantity = 2,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сахар",
                                            quantity = 5,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "соль",
                                            quantity = 1,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "чай",
                                            quantity = 5,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сухой завтрак",
                                            quantity = 3,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "какао",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "масло растительное",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "масло кунжутное",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "печенье",
                                            quantity = 3,
                                            unit = "шт",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    CatalogDatabase(
                            id = 4,
                            title = "Хозтовары - запас",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "щетки детские",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "щетки взрослые",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "прокладки",
                                            quantity = 3,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "резинки детские - 100шт",
                                            quantity = 1,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "меламиновая губка",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "гель для умывания",
                                            quantity = 3,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "мешки для мусора",
                                            quantity = 5,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "пакеты упаковочные - 30шт",
                                            quantity = 3,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "паста детская",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "паста взрослая",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "туалетная бумага",
                                            quantity = 12,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "мыло", quantity = 6, unit = "шт", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "шампунь",
                                            quantity = 2,
                                            unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "белизна", quantity = 3, unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "губки", quantity = 6, unit = "шт",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "бумажные полотенца", quantity = 1, unit = "шт",
                                            isSelected =
                                            false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    // Справочник
                    CatalogDatabase(
                            id = 5,
                            title = "Хлеб",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "хлеб белый",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "хлеб черный",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "батон",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "булки",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 6,
                            title = "Крупы",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "рис", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "геркулес",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "гречка", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "пшено", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "манка", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "хлопья для завтрака",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "горох", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "фасоль", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "спагетти",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "макароны",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "перловка",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    CatalogDatabase(
                            id = 7,
                            title = "Мясо",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "Свинина",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "говядина",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "мясо суповое",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "куриные грудки",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "куриные ножки",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "куры",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "фарш",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "печень",
                                            quantity = 0,
                                            unit = "кг",
                                            isSelected = false
                                               ),
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    CatalogDatabase(
                            id = 8,
                            title = "Рыба",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "рыба морская",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "рРыба речная",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "рыбные консервы",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "деликатесы рыбные",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 9,
                            title = "Овощи",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "картошка",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "морковка",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "лук",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "свекла",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "капуста",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "чеснок",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "зелень",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "замороженные овощи",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "грибы",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 10,
                            title = "Сухофрукты",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "изюм", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "курага", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "чернослив",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "манго",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "компотная смесь",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 11,
                            title = "Консервы",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "маринованные овощи",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "томатный соус",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                    CatalogItem(
                                            title = "горошек",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "кукуруза",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "оливки",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               )
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 12,
                            title = "Молочные продукты",
                            order = 0,
                            list = listOf(
                                    CatalogItem(
                                            title = "молоко", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "масло", quantity = 0, unit = "", isSelected =
                                    false
                                               ),
                                    CatalogItem(
                                            title = "сыр",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сметана",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "творог",
                                            quantity = 0,
                                            unit = "",
                                            isSelected = false
                                               ),
                                    CatalogItem(
                                            title = "сгущенное молоко",
                                            quantity = 0,
                                            unit = "",
                                            isSelected =
                                            false
                                               ),
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ), CatalogDatabase(
                    id = 13,
                    title = "Бакалея",
                    order = 0,
                    list = listOf(
                            CatalogItem(
                                    title = "яйца", quantity = 0, unit = "", isSelected =
                            false
                                       ),
                            CatalogItem(
                                    title = "мука", quantity = 0, unit = "", isSelected =
                            false
                                       ),
                            CatalogItem(
                                    title = "сахар",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "соль",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "пряности",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "кофе",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(title = "чай", quantity = 0, unit = "", isSelected = false),
                            CatalogItem(
                                    title = "какао",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "масло растительное",
                                    quantity = 0,
                                    unit = "",
                                    isSelected =
                                    false
                                       ),
                            CatalogItem(
                                    title = "крахмал",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(title = "мед", quantity = 0, unit = "", isSelected = false),
                            CatalogItem(
                                    title = "уксус",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "дрожжи",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       )
                                 ),
                    lastModificationDate = 0,
                    creationDate = 0
                                                     ), CatalogDatabase(
                    id = 14,
                    title = "Для хозяйства",
                    order = 0,
                    list = listOf(
                            CatalogItem(
                                    title = "стиральный порошок",
                                    quantity = 0,
                                    unit = "",
                                    isSelected =
                                    false
                                       ),
                            CatalogItem(
                                    title = "мыло детское", quantity = 0, unit = "", isSelected =
                            false
                                       ),
                            CatalogItem(
                                    title = "мыло",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "туалетная бумага",
                                    quantity = 0,
                                    unit = "",
                                    isSelected =
                                    false
                                       ),
                            CatalogItem(
                                    title = "средство для мытья посуды", quantity = 0, unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "чистящий порошок",
                                    quantity = 0,
                                    unit = "",
                                    isSelected =
                                    false
                                       ),
                            CatalogItem(
                                    title = "перчатки",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "губки",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "крем для обуви",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "шампунь",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "зубная паста",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "деская зубная паста",
                                    quantity = 0,
                                    unit = "",
                                    isSelected =
                                    false
                                       ),
                            CatalogItem(
                                    title = "зубная нить",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       ),
                            CatalogItem(
                                    title = "ватные диски",
                                    quantity = 0,
                                    unit = "",
                                    isSelected = false
                                       )
                                 ),
                    lastModificationDate = 0,
                    creationDate = 0
                                                                       )
                  )
    
    fun requestEnglishData() =
            listOf(
                    CatalogDatabase(
                            id = 1,
                            title = "Produce",
                            order = 0,
                            list = listOf(
                                    CatalogItem(title = "bananas"),
                                    CatalogItem(title = "apples"),
                                    CatalogItem(title = "oranges"),
                                    CatalogItem(title = "avocados"),
                                    CatalogItem(title = "peaches"),
                                    CatalogItem(title = "pineapple"),
                                    CatalogItem("broccoli"),
                                    CatalogItem("carrots"),
                                    CatalogItem("celery"),
                                    CatalogItem("cucumbers"),
                                    CatalogItem("garlic"),
                                    CatalogItem("grapefruit"),
                                    CatalogItem("grapes"),
                                    CatalogItem("lemons"),
                                    CatalogItem("melons"),
                                    CatalogItem("mushrooms"),
                                    CatalogItem("oranges"),
                                    CatalogItem("peppers"),
                                    CatalogItem("potatoes"),
                                    CatalogItem("tomatoes"),
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 2,
                            title = "Bread/Bakery",
                            order = 0,
                            list = listOf(
                                    CatalogItem("bread"),
                                    CatalogItem("cake"),
                                    CatalogItem("donuts"),
                                    CatalogItem("pie"),
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    
                    CatalogDatabase(
                            id = 3,
                            title = "Meat",
                            order = 0,
                            list = listOf(
                                    CatalogItem("bacon"),
                                    CatalogItem("beef"),
                                    CatalogItem("chicken"),
                                    CatalogItem("fish"),
                                    CatalogItem("ham"),
                                    CatalogItem("pork"),
                                    CatalogItem("turkey")
                            
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    
                    CatalogDatabase(
                            id = 4,
                            title = "Fridge",
                            order = 0,
                            list = listOf(
                                    CatalogItem("butter"),
                                    CatalogItem("cheese"),
                                    CatalogItem("cream cheese"),
                                    CatalogItem("eggs"),
                                    CatalogItem("milk"),
                                    CatalogItem("yogurt")
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    
                    CatalogDatabase(
                            id = 5,
                            title = "Frozen",
                            order = 0,
                            list = listOf(
                                    CatalogItem("chicken"),
                                    CatalogItem("desserts"),
                                    CatalogItem("fish"),
                                    CatalogItem("ice"),
                                    CatalogItem("vegetables"),
                                    CatalogItem("pizza")
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                    CatalogDatabase(
                            id = 6,
                            title = "Baking",
                            order = 0,
                            list = listOf(
                                    CatalogItem("baking powder"),
                                    CatalogItem("baking soda"),
                                    CatalogItem("sugar"),
                                    CatalogItem("salt"),
                                    CatalogItem("peper"),
                                    CatalogItem("yogurt")
                                         ),
                            lastModificationDate = 0,
                            creationDate = 0
                                   ),
                  )
    
  }
}