package dev.tehbrian.tehlib.paper.configurate;

import dev.tehbrian.tehlib.configurate.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ConfigLoader {
	private final JavaPlugin plugin;

	public ConfigLoader(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean load(final List<Loadable> toLoad) {
		for (final Loadable data : toLoad) {
			final Path dataPath = this.plugin.getDataPath();
			final Path savePath = dataPath.resolve(data.filename());

			if (!Files.exists(savePath)) {
				this.plugin.saveResource(data.filename(), false);
			}

			final Config<?> config = data.config();
			try {
				config.load();
			} catch (final ConfigurateException e) {
				this.plugin.getSLF4JLogger().error(
						"Exception caught during config load for {}",
						config.wrapper().path()
				);
				this.plugin.getSLF4JLogger().error("Please check your config.");
				this.plugin.getSLF4JLogger().error("Printing stack trace:", e);
				return false;
			}

			if (!data.versioned()) {
				// skip version check.
				continue;
			}

			final int loadedVersion = config.wrapper().rootNode().node("version").getInt();
			if (loadedVersion != data.version()) {
				this.plugin.getSLF4JLogger().warn(
						"Config {} is versioned {}, but the current version is {}.",
						data.filename(), loadedVersion, data.version()
				);

				int tag = -1;
				String archiveFilename;
				Path archivePath;
				do {
					tag += 1;
					archiveFilename = data.filenameBase() + ".old-" + tag + "." + data.filenameExt();
					archivePath = dataPath.resolve(archiveFilename);
				} while (Files.exists(archivePath));

				this.plugin.getSLF4JLogger().warn(
						"Regenerating {} and archiving the old config to {}.",
						data.filename(), archiveFilename
				);

				try {
					Files.move(savePath, archivePath);
				} catch (final IOException e) {
					this.plugin.getSLF4JLogger().error("Failed to move the old config.");
					this.plugin.getSLF4JLogger().error("Printing stack trace:", e);
					return false;
				}

				this.plugin.saveResource(data.filename(), false);
			}
		}

		this.plugin.getSLF4JLogger().info("Successfully loaded configuration.");
		return true;
	}

	public static final class Loadable {
		private final String filenameBase;
		private final String filenameExt;
		private final Config<?> config;
		private final int version;

		private Loadable(
				final String filenameBase, final String filenameExt,
				final Config<?> config, final int version
		) {
			this.filenameBase = filenameBase;
			this.filenameExt = filenameExt;
			this.config = config;
			this.version = version;
		}

		public static Loadable of(final String filename, final Config<?> config) {
			return ofVersioned(filename, config, -1);
		}

		public static Loadable ofVersioned(final String filename, final Config<?> config, final int version) {
			final var split = filename.split("\\.");
			return new Loadable(split[0], split[1], config, version);
		}

		public String filenameBase() {
			return this.filenameBase;
		}

		public String filenameExt() {
			return this.filenameExt;
		}

		public Config<?> config() {
			return this.config;
		}

		public int version() {
			return this.version;
		}

		public String filename() {
			return this.filenameBase() + "." + this.filenameExt();
		}

		public boolean versioned() {
			return this.version() > 0;
		}
	}
}
