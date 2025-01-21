package top.superxuqc.mcmod.common;

import java.util.UUID;

public interface EntityModI {
    void setByPlayer(UUID playerUuid);

    UUID getByPlayer();
}
