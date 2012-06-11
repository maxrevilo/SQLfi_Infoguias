
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.api.templates.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import com.avaje.ebean._
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(Seq[Any](/*3.2*/main("Welcome to Play 2.0")/*3.29*/ {_display_(Seq[Any](format.raw/*3.31*/("""
    
    """),_display_(Seq[Any](/*5.6*/play20/*5.12*/.welcome(message, style = "Scala"))),format.raw/*5.46*/("""
    message
""")))})),format.raw/*7.2*/("""
"""))}
    }
    
    def render(message:String) = apply(message)
    
    def f:((String) => play.api.templates.Html) = (message) => apply(message)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Sun Jun 10 19:54:17 VET 2012
                    SOURCE: /home/maxrevilo/TDS/infoguias/app/views/index.scala.html
                    HASH: a58a6209751ebb1afe937e33dfada5f2246bee75
                    MATRIX: 755->1|849->18|886->21|921->48|960->50|1005->61|1019->67|1074->101|1118->115
                    LINES: 27->1|30->1|32->3|32->3|32->3|34->5|34->5|34->5|36->7
                    -- GENERATED --
                */
            