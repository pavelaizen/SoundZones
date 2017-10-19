package com.gm.soundzones.loader

import android.text.Editable
import android.text.Html
import org.xml.sax.XMLReader

/**
 * Created by Pavel Aizendorf on 19/10/2017.
 */
class HtmlTagHandler : Html.TagHandler {
    val bullet = "\u2B24"
    override fun handleTag(opening: Boolean, tag: String?, output: Editable?, xmlReader: XMLReader?) {
        if (tag.equals("ul") && !opening) {
            output?.append("\n");
        }
        if (tag.equals("li") && opening) {
            output?.append("\n\t##");
        }
    }

}