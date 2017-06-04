import com.easy.kotlin.kotlin_script_template.*

"""
${include("header")}
<p>Locale:
<a href="/?locale=fr">FR</a> |
<a href="/?locale=en">EN</a> |
<a href="/?locale=zh">中文</a>

</p>
<h1>${i18n("title")}</h1>
${include("users", mapOf(Pair("users", users)))}
${include("footer")}
"""
