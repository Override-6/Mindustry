package mindustry.server;

import fr.linkit.api.application.config.ApplicationInstantiationException;
import fr.linkit.server.ServerApplication;
import fr.linkit.server.config.ServerApplicationConfigBuilder;
import fr.linkit.server.config.ServerApplicationConfiguration;
import scala.collection.JavaConverters;
import scala.collection.immutable.Seq;

import java.util.Arrays;

public class ServerAppLauncher {

    public static ServerApplication launchApp() throws ApplicationInstantiationException {
        ServerApplicationConfiguration config = new ServerApplicationConfigBuilder() {
            @Override
            public String resourcesFolder() {
                return System.getenv("LinkitHome");
            }
        }.buildConfig();
        Seq<Class<?>> classes = JavaConverters.asScala(Arrays.<Class<?>>asList(mindustry.game.Schematics.class)).toSeq();
        return ServerApplication.launch(config, classes);
    }

}