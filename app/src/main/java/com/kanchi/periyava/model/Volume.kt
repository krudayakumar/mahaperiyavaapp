package com.kanchi.periyava.model


/**
 * Created by m84098 on 2/15/18.
 */
data class Volume(var id: Type,
                  var title: String,
                  var short_desc:String,
                  var desc:String,
                  var image: String,
                  var link: String,
                  var visibility: Boolean,
                  var titleVisibility: Boolean = false) {
    enum class Type {
        NONE,
        VOLUME_1,
        VOLUME_2,
        VOLUME_3,
        VOLUME_4,
        VOLUME_5,
        VOLUME_6,
    }
}
