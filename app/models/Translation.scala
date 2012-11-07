package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val text: String,
  val authorId: ObjectId,
  val active: Boolean = false) {

  def toMap = Map(
    "code" -> code,
    "text" -> text,
    "author" -> "",
    "active" -> active)
}

case class Entry(
  val name: String,
  val description: String,
  val projectId: ObjectId,
  val translations: List[Translation] = Nil,
  @Key("_id") val id: ObjectId = new ObjectId) {

  def project = ProjectDAO.findOneById(projectId) get

  def translationsFixed = translations ++ LanguageDAO.findAllByProjectId(projectId).map(_.code).diff(translations.map(_.code)).map { code =>
    Translation(code, "", project.adminId, true)
  }

  def percentage = LanguageDAO.findAllByProjectId(projectId).filterNot { lang =>
    translations.exists(_.code == lang.code)
  }.length / translations.length * 100


  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "description" -> description,
    "translations" -> translationsFixed.map(_.toMap),
    "percentage" -> percentage)
}



//scala> val langs = List("de", "en", "fr")                                            
 //failed                                                                             │langs: List[java.lang.String] = List(de, en, fr)                                     
//[error] Total time: 0 s, completed Nov 6, 2012 8:49:31 AM                           │                                                                                     
//[translator] $ test                                                                 │scala> val trans = List(                                                             
//[info] Compiling 1 Scala source to /home/dennis/projects/translator/target/scala-2.9│     | ("de" -> "Hallo"),                                                            
//.1/test-classes...                                                                  │     | ("en" -> "Hello"))                                                            
//[error] /home/dennis/projects/translator/test/ImporterSpec.scala:11: value extension│trans: List[(java.lang.String, java.lang.String)] = List((de,Hallo), (en,Hello))     
 //is not a member of java.io.File                                                    │                                                                                     
//[error]       println(file.extension)                                               │scala> langs filterNot { l =>                                                        
//[error]                    ^                                                        │     | trans.find(_._1 == l)                                                         
//[error] one error found                                                             │     | }                                                                             
//[error] {file:/home/dennis/projects/translator/}translator/test:compile: Compilation│<console>:11: error: type mismatch;                                                  
 //failed                                                                             │ found   : Option[(java.lang.String, java.lang.String)]                              
//[error] Total time: 1 s, completed Nov 6, 2012 8:49:39 AM                           │ required: Boolean                                                                   
//[translator] $ run 8000                                                             │              trans.find(_._1 == l)                                                  
                                                                                    //│                        ^                                                            
//--- (Running the application from SBT, auto-reloading is enabled) ---               │                                                                                     
                                                                                    //│scala> langs filterNot { l =>                                                        
//#logback.classic pattern: %d [%t] %-5p %logger{25} - %m%n                           │     | trans.exists(_._1 == l)                                                       
//#logback.classic pattern: %coloredLevel %logger{15} - %message%n%xException{5}      │     | }                                                                             
//[info] play - Listening for HTTP on port 8000...                                    │res1: List[java.lang.String] = List(fr)                                              
                                                                                    //│                                                                                     
//(Server started, use Ctrl+D to stop and go back to the console...)                  │scala>                                                                               
                                                                                    //│                                                                                     
//#logback.classic pattern: %coloredLevel %logger{15} - %message%n%xException{5}      │             
