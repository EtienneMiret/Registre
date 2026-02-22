import org.gradle.api.Action
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.Serializable

class ConfigureVersionResourcesAction(
  private val version: String,
  private val gitHash: String,
): Action<ProcessResources>, Serializable {

  override fun execute(task: ProcessResources) {
    task.inputs.property("version", version)
    task.inputs.property("gitHash", gitHash)
    task.filteringCharset = "UTF-8"

    task.filesMatching("**/version.properties") {
      expand(mapOf(
          "version" to version,
          "gitHash" to gitHash,
      ))
    }
  }

}
