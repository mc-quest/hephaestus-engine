package team.unnamed.hephaestus;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.hephaestus.commands.HephaestusCommand;
import team.unnamed.hephaestus.commands.part.ModelAnimationPart;
import team.unnamed.hephaestus.commands.part.ModelPart;
import team.unnamed.hephaestus.entity.ModelLivingEntityAnimator;
import team.unnamed.hephaestus.entity.ModelLivingEntitySpawner;
import team.unnamed.hephaestus.model.Model;
import team.unnamed.hephaestus.model.animation.ModelAnimation;
import team.unnamed.hephaestus.model.entity.ModelEntityAnimator;
import team.unnamed.hephaestus.model.entity.ModelEntitySpawner;
import team.unnamed.hephaestus.reader.ModelReader;
import team.unnamed.hephaestus.reader.blockbench.BlockbenchModelReader;
import team.unnamed.hephaestus.resourcepack.HephaestusResourcePackExporter;
import team.unnamed.hephaestus.resourcepack.ModelRegistry;
import team.unnamed.hephaestus.resourcepack.ResourcePackExporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class AnimationEnginePlugin extends JavaPlugin {

    // TODO: REPLACE THIS SHIT, USE DEPENDENCY INJECTION
    private static ModelEntitySpawner spawner;
    private static ModelEntityAnimator animator;

    public static ModelEntitySpawner getSpawner() {
        return spawner;
    }

    public static ModelEntityAnimator getAnimator() {
        return animator;
    }

    @Override
    public void onEnable() {

        try {
            this.saveResourceDir("models", "pegasus");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        spawner = new ModelLivingEntitySpawner();
        animator = new ModelLivingEntityAnimator(this);

        ModelRegistry modelRegistry = new ModelRegistry();

        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());
        partInjector.bindFactory(Model.class, (name, modifiers) ->
                new ModelPart(modelRegistry, name)
        );
        partInjector.bindFactory(ModelAnimation.class, (name, modifiers) ->
                new ModelAnimationPart(modelRegistry, name)
        );

        AnnotatedCommandTreeBuilder commandBuilder = new AnnotatedCommandTreeBuilderImpl(
                partInjector
        );
        
        CommandManager commandManager = new BukkitCommandManager(this.getName());

        commandManager.registerCommands(commandBuilder.fromClass(new HephaestusCommand()));

        ModelReader modelReader = new BlockbenchModelReader();
        ResourcePackExporter resourcePackExporter = new HephaestusResourcePackExporter();

        File modelsDirectory = new File(this.getDataFolder(), "models");

        if (!modelsDirectory.exists() && !modelsDirectory.mkdirs()) {
            throw new IllegalStateException("Cannot create models folder");
        }

        File[] contents = modelsDirectory.listFiles();

        List<Model> models = new ArrayList<>();
        if (contents != null) {
            for (File modelFile : contents) {
                if (modelFile.isDirectory()) {
                    try {
                        Model model = modelReader.read(modelFile);
                        this.getLogger().log(Level.INFO, "Loaded model " + model.getName());
                        models.add(model);
                    } catch (IOException exception) {
                        this.getLogger().log(
                                Level.WARNING,
                                "Cannot read model data from directory " + modelFile.getName(),
                                exception
                        );
                    }
                }
            }
        }

        this.getLogger().log(Level.INFO, "Successfully loaded " + models.size() + " models!");

        try {
            resourcePackExporter.export(this.getDataFolder(), "hephaestus-generated", models)
                    .forEach(model -> {
                        getLogger().info("Registered model " + model.getName());
                        modelRegistry.register(model);
                    });
        } catch (IOException exception) {
            this.getLogger().log(
                    Level.SEVERE,
                    "Could not generate resource pack",
                    exception
            );
        }
    }

    private void saveResourceDir(String path, String resource) throws IOException {
        File folder = new File(this.getDataFolder().getPath(), path + File.separator + resource);
        if (folder.exists()) return;
        folder.mkdirs();

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if (jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(resource + "/") && !name.equals(resource + "/")) {
                    String fileName = name.split(resource + "/")[1];
                    Files.copy(
                            this.getClass().getClassLoader().getResourceAsStream(resource + "/" + fileName),
                            Paths.get(folder.getPath() + File.separator + fileName)
                    );
                }
            }
            jar.close();
        }
    }
}