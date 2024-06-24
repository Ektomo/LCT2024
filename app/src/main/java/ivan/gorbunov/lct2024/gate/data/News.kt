package ivan.gorbunov.lct2024.gate.data

import ivan.gorbunov.lct2024.R

data class HomeItem(
    val id: String,
    val cat: HomeCategories,
    val title: String,
    val description: String,
//    val data: Map<String, String> = mapOf(),
    val img: Int? = null
)

enum class HomeCategories {
    News, Coaches, Workouts, Receipts
}

val homeItems = listOf(
    // Новости
    HomeItem(
        id = "1",
        cat = HomeCategories.News,
        title = "Открытие нового фитнес-клуба",
        description = """
            В центре города открылся новый современный фитнес-клуб, предлагающий широкий спектр услуг: тренажерный зал, групповые занятия, бассейн и спа. В честь открытия действуют специальные предложения и скидки на абонементы.
        """.trimIndent(),
        img = R.drawable.club
    ),
    HomeItem(
        id = "2",
        cat = HomeCategories.News,
        title = "Предстоящий спортивный фестиваль",
        description = """
            Планируется крупный фитнес-фестиваль на свежем воздухе, включающий мастер-классы, соревнования и выступления профессиональных спортсменов. Мероприятие пройдет в городском парке и будет доступно для всех желающих.
        """.trimIndent(),
        img =R.drawable.fest
    ),
    HomeItem(
        id = "3",
        cat = HomeCategories.News,
        title = "Открытие магазина спортивного инвентаря",
        description = """
            В нашем городе открылся новый магазин спортивного инвентаря, где можно найти все необходимое для занятий спортом: от одежды и обуви до тренажеров и аксессуаров. В честь открытия действует акция - скидки до 30%.
        """.trimIndent(),
        img = R.drawable.new_store
    ),
    HomeItem(
        id = "4",
        cat = HomeCategories.News,
        title = "Фитнес-челлендж для всех желающих",
        description = """
            Присоединяйтесь к фитнес-челленджу и проверьте свои силы! Вас ждут различные упражнения и задания, разработанные профессиональными тренерами. Участвуйте и получайте призы и подарки от спонсоров мероприятия.
        """.trimIndent(),
        img = R.drawable.event
    ),

    // Тренеры
    HomeItem(
        id = "5",
        cat = HomeCategories.Coaches,
        title = "Иван Иванов",
        description = """
            Достижения: Мастер спорта по легкой атлетике.
            Профиль: Персональный тренер по фитнесу и бодибилдингу.
            Специализация: Силовые тренировки и функциональный тренинг.
            Образование: Всероссийский государственный университет физической культуры.
        """.trimIndent(),
        img = R.drawable.fitnesm1
    ),
    HomeItem(
        id = "6",
        cat = HomeCategories.Coaches,
        title = "Мария Петрова",
        description = """
            Достижения: Чемпионка мира по кроссфиту.
            Профиль: Персональный тренер по кроссфиту и общей физической подготовке.
            Специализация: Высокоинтенсивные интервальные тренировки (HIIT).
            Образование: Российский университет физической культуры, спорта и туризма.
        """.trimIndent(),
        img = R.drawable.fitnessg1
    ),
    HomeItem(
        id = "7",
        cat = HomeCategories.Coaches,
        title = "Алексей Смирнов",
        description = """
            Достижения: Призер Олимпийских игр по плаванию.
            Профиль: Персональный тренер по плаванию и аквааэробике.
            Специализация: Тренировки по плаванию и аквааэробике.
            Образование: Московская государственная академия физической культуры.
        """.trimIndent(),
        img = R.drawable.fitnesm2
    ),
    HomeItem(
        id = "8",
        cat = HomeCategories.Coaches,
        title = "Екатерина Сидорова",
        description = """
            Достижения: Многократная чемпионка России по йоге.
            Профиль: Инструктор по йоге и пилатесу.
            Специализация: Йога, пилатес и медитация.
            Образование: Институт Восточной медицины.
        """.trimIndent(),
        img = R.drawable.fitnessg2
    ),

    // Рецепты
    HomeItem(
        id = "9",
        cat = HomeCategories.Receipts,
        title = "Салат с киноа и курицей",
        description = """
            1. Приготовьте киноа в соответствии с инструкцией на упаковке.
            2. Обжарьте куриную грудку на гриле до готовности.
            3. Нарежьте овощи (помидоры, огурцы, авокадо) кубиками.
            4. Смешайте все ингредиенты в большой миске.
            5. Приправьте оливковым маслом, солью и перцем по вкусу.
        """.trimIndent(),
        img = R.drawable.recipe1
    ),
    HomeItem(
        id = "10",
        cat = HomeCategories.Receipts,
        title = "Смузи боул с ягодами",
        description = """
            1. В блендере смешайте замороженные ягоды, банан и йогурт до однородной массы.
            2. Перелейте смесь в миску.
            3. Украсьте сверху свежими ягодами, семенами чиа и орехами.
            4. Подавайте немедленно.
        """.trimIndent(),
        img = R.drawable.recipe2
    ),
    HomeItem(
        id = "11",
        cat = HomeCategories.Receipts,
        title = "Лосось на гриле с аспарагусом",
        description = """
            1. Замаринуйте лосось в оливковом масле, лимонном соке, соли и перце.
            2. Разогрейте гриль и обжарьте лосось с каждой стороны по 4-5 минут.
            3. Обжарьте аспарагус на гриле до мягкости.
            4. Подавайте лосось с аспарагусом и ломтиком лимона.
        """.trimIndent(),
        img = R.drawable.recipe3
    ),
    HomeItem(
        id = "12",
        cat = HomeCategories.Receipts,
        title = "Протеиновые панкейки с бананом",
        description = """
            1. В миске смешайте овсяную муку, протеиновый порошок, разрыхлитель и соль.
            2. Добавьте в смесь молоко и яйцо, хорошо перемешайте.
            3. Разогрейте сковороду и выпекайте панкейки до золотистого цвета.
            4. Подавайте с нарезанным бананом и медом.
        """.trimIndent(),
        img = R.drawable.recipe4
    )
)

//val mockGateNews = listOf(
//    HomeItem(
//        "1",
//        HomeCategories.News,
//        mapOf("title" to "Новая акция"),
//        "https://i.imgur.com/CzXTtJV.jpg"
//    ),
//    HomeItem(
//        "2",
//        HomeCategories.News,
//        mapOf("title" to "Новый клуб"),
//        "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
//    ),
//    HomeItem(
//        "3",
//        HomeCategories.News,
//        mapOf("title" to "Первый пауэрлифтер"),
//        "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg"
//    ),
//    HomeItem(
//        "4",
//        HomeCategories.Coaches,
//        mapOf("title" to "Егор Федоров", "description" to "Безумно долго может ячто-то делать"),
//        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
//    ),
//    HomeItem(
//        "5",
//        HomeCategories.Coaches,
//        mapOf("title" to "Лиза Золтон", "description" to "Безумно долго может ячто-то делать"),
//        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
//    ),
//    HomeItem(
//        "6",
//        HomeCategories.Coaches,
//        mapOf("title" to "Абрахам Наклоняев", "description" to "Безумно долго может ячто-то делать"),
//        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
//    ),
//    HomeItem(
//        "7",
//        HomeCategories.Workouts,
//        mapOf("title" to "Дома 30 минут", "description" to "Когда времени мало и дома место есть"),
//        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
//    ),
//    HomeItem(
//        "8",
//        HomeCategories.Workouts,
//        mapOf("title" to "Дома 60 минут", "description" to "Когда время есть и дома место есть"),
//        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
//    ),
//    HomeItem(
//        "9",
//        HomeCategories.Workouts,
//        mapOf("title" to "Улица 30 минут", "description" to "Когда времени мало и дома места нет"),
//        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
//    ),
//    HomeItem(
//        "10",
//        HomeCategories.Receipts,
//        mapOf("title" to "Кускус с чем-то", "description" to "Делать долго, но полезно"),
//        "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg"
//    ),
//    HomeItem(
//        "11",
//        HomeCategories.Receipts,
//        mapOf("title" to "Филе", "description" to "Делать долго, но полезно"),
//        "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg"
//    ),
//    HomeItem(
//        "12",
//        HomeCategories.Receipts,
//        mapOf("title" to "Раншманшпан", "description" to "Делать долго, но полезно"),
//        "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg"
//    ),
//)
