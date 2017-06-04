import com.easy.kotlin.kotlin_script_template.*

"""
<ul>
${users.joinToLine { "<li>${include("user", mapOf(Pair("user", it)))}</li>" }}
</ul>
"""
