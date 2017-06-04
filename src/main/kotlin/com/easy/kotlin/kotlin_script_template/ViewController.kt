package com.easy.kotlin.kotlin_script_template

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.ArrayList

@Controller
class ViewController {

    @GetMapping("/")
    fun render(model: Model): String {
        val list = ArrayList<User>()
        list.add(User("Jason", "Chen"))
        model.addAttribute("users",  list)
        return "index"
    }

}
