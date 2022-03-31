package com.example.composothon

data class SquadObject(
    var name: String,
    var squad: String = "",
    var photo: String = "defaultman.jpg",
    var logo: String = "nucleus.jpg"
) {
    override fun equals(other: Any?): Boolean {

        if ((squad==null || squad.length==0) && this.name.contains((other as SquadObject).name)) {
            return true
        }
        else if ((squad==null || squad.length==0) && this.name.contains((other as SquadObject).name) && squad.contains((other as SquadObject).name))
        {
            return true
        }
        return super.equals(other)

    }
}