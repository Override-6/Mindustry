import fr.linkit.engine.gnom.persistence.config.PersistenceConfigBuilder
import fr.linkit.engine.internal.utils.Identity
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.ctype.{ContentType, MappableContent}

import java.lang.reflect.Field

//Start Of Context
val builder: PersistenceConfigBuilder = null

import builder._

//ENd Of Context
setTConverter[MappableContent, (ContentType, String)](c => (c.getContentType, c.name)) {
    case (contentType, name) => Vars.content.getByName(contentType, name)
}
val clazz = classOf[Blocks]
clazz.getFields.foreach(addContextField)

def addContextField(field: Field): Unit = {
    val value = field.get(null)
    val id = field.getName.hashCode
    putContextReference(field.getName.hashCode, Identity(value))
    println(s"Bound context field value $field with identifier $id")
}
