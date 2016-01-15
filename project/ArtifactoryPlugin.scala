import sbt._
import sbt.Keys._

object ArtifactoryPlugin {
  private val BlazeReleasesRepo = "Blaze Releases" at "http://wehkamp.artifactoryonline.com/wehkamp/blaze-releases"
  private val BlazeSnapshotsRepo = "Blaze Snapshots" at "http://wehkamp.artifactoryonline.com/wehkamp/blaze-snapshots"

  def projectSettings = Seq(
    resolvers ++= Seq(BlazeReleasesRepo, BlazeSnapshotsRepo),
    credentials += repositoryCredentials,
    scalaVersion := "2.11.7",
    crossScalaVersions := List(scalaVersion.value),
    publishTo <<= version { version =>
      Some {
        if (version.contains("-SNAPSHOT")) BlazeSnapshotsRepo
        else BlazeReleasesRepo
      }
    }
  )

  private def repositoryCredentials: Credentials = {
    (
      for {
        user <- sys.env.get("BLAZE_ARTIFACTORY_USER");
        password <- sys.env.get("BLAZE_ARTIFACTORY_PASSWORD")
       }
       yield Credentials("Artifactory Realm", "wehkamp.artifactoryonline.com", user, password)
    )
    .getOrElse(Credentials(Path.userHome / ".ivy2" / "blaze-artifactory.credentials"))
  }
}
