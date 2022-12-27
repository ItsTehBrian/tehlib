package dev.tehbrian.tehlib.paper.user;

import dev.tehbrian.tehlib.user.AbstractUser;

import java.util.UUID;

/**
 * Represents a user.
 */
public abstract class PaperUser extends AbstractUser<UUID> {

  /**
   * @param uuid the uuid
   */
  public PaperUser(final UUID uuid) {
    super(uuid);
  }

}
