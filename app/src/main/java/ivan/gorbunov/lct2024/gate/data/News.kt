package ivan.gorbunov.lct2024.gate.data

data class HomeItem(
    val id: String,
    val cat: HomeCategories,
    val data: Map<String, String> = mapOf(),
    val img: String? = null
)

enum class HomeCategories {
    News, Coaches, Workouts, Receipts
}

val mockGateNews = listOf(
    HomeItem(
        "1",
        HomeCategories.News,
        mapOf("title" to "Новая акция"),
        "https://i.imgur.com/CzXTtJV.jpg"
    ),
    HomeItem(
        "2",
        HomeCategories.News,
        mapOf("title" to "Новый клуб"),
        "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg"
    ),
    HomeItem(
        "3",
        HomeCategories.News,
        mapOf("title" to "Первый пауэрлифтер"),
        "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg"
    ),
    HomeItem(
        "4",
        HomeCategories.Coaches,
        mapOf("title" to "Егор Федоров", "description" to "Безумно долго может ячто-то делать"),
        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
    ),
    HomeItem(
        "5",
        HomeCategories.Coaches,
        mapOf("title" to "Лиза Золтон", "description" to "Безумно долго может ячто-то делать"),
        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
    ),
    HomeItem(
        "6",
        HomeCategories.Coaches,
        mapOf("title" to "Абрахам Наклоняев", "description" to "Безумно долго может ячто-то делать"),
        "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg"
    ),
    HomeItem(
        "7",
        HomeCategories.Workouts,
        mapOf("title" to "Дома 30 минут", "description" to "Когда времени мало и дома место есть"),
        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
    ),
    HomeItem(
        "8",
        HomeCategories.Workouts,
        mapOf("title" to "Дома 60 минут", "description" to "Когда время есть и дома место есть"),
        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
    ),
    HomeItem(
        "9",
        HomeCategories.Workouts,
        mapOf("title" to "Улица 30 минут", "description" to "Когда времени мало и дома места нет"),
        "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg"
    ),
    HomeItem(
        "10",
        HomeCategories.Receipts,
        mapOf("title" to "Кускус с чем-то", "description" to "Делать долго, но полезно"),
        "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg"
    ),
    HomeItem(
        "11",
        HomeCategories.Receipts,
        mapOf("title" to "Филе", "description" to "Делать долго, но полезно"),
        "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg"
    ),
    HomeItem(
        "12",
        HomeCategories.Receipts,
        mapOf("title" to "Раншманшпан", "description" to "Делать долго, но полезно"),
        "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg"
    ),
)
