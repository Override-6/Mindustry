package mindustry.desktop;

import fr.linkit.client.ClientApplication;
import fr.linkit.client.config.ClientApplicationConfigBuilder;
import fr.linkit.client.config.ClientApplicationConfiguration;
import scala.collection.JavaConverters;
import scala.collection.immutable.Seq;

import java.util.Arrays;

public class ClientAppLauncher {

    public static ClientApplication launchApp() {
        ClientApplicationConfiguration config = new ClientApplicationConfigBuilder() {
            @Override
            public String resourcesFolder() {
                return System.getenv("LinkitHome");
            }
        }.buildConfig();
        Seq<Class<?>> classes = JavaConverters.asScala(Arrays.<Class<?>>asList(
                mindustry.game.Schematics.class, arc.struct.IntSet.class,
                arc.backend.sdl.SdlGL30.class
        )).toSeq();
        return ClientApplication.launch(config, classes);
    }

}
