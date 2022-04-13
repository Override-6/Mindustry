import arc.math.Interp
import fr.linkit.engine.gnom.persistence.config.PersistenceConfigBuilder
import fr.linkit.engine.internal.utils.{Identity, ScalaUtils}
import mindustry.Vars
import mindustry.content.{Blocks, Fx, UnitTypes}
import mindustry.ctype.{ContentType, MappableContent}
import mindustry.gen.Sounds

import java.lang.reflect.{Field, InaccessibleObjectException, Modifier}
import scala.reflect.{ClassTag, classTag}

//Start Of Context
val builder: PersistenceConfigBuilder = null

import builder._

//ENd Of Context
setTConverter[MappableContent, (ContentType, String)](c => (c.getContentType, c.name)) {
    case (contentType, name) => Vars.content.getByName(contentType, name)
}

bindAllStaticFields[Blocks]
bindAllStaticFields[Fx]
bindAllStaticFields[UnitTypes]
bindAllStaticFields[Sounds]
bindAllStaticFields[Interp]

def addContextField(field: Field): Unit = {
    val value = field.get(null)
    val id    = field.getName.hashCode
    putContextReference(id, Identity(value))
}
def bindAllStaticFields[T: ClassTag]: Unit = {
    val clazz  = classTag[T].runtimeClass
    val fields = retrieveAllStaticFields(clazz)
    println(s"found ${fields.length} fields to register in $clazz.")
    fields.foreach(addContextField)
}

def retrieveAllStaticFields(clazz: Class[_]): Array[Field] = {
    var superClass  = clazz
    var superFields = Array.empty[Field]
    while (superClass != null) {
        superFields ++= superClass.getDeclaredFields
        superClass = superClass.getSuperclass
    }
    superFields
            .filter(f => Modifier.isStatic(f.getModifiers) && setAccessible(f))
}

def setAccessible(field: Field): Boolean = try {
    field.setAccessible(true)
    true
} catch {
    case _: InaccessibleObjectException => false
}
