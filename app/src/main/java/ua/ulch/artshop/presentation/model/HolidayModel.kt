package ua.ulch.artshop.presentation.model

data class HolidayModel(
    var id: Int = 0,
    var year: String? = null,
    var dayOfWeek: String? = null,
    var dayOfMonth: String? = null,
    var month: String? = null,
    var title: String?,
    var description: String? = null,
    var category: String? = null,
    var imageUrl: String?
)