package top.superxuqc.mcmod.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.superxuqc.mcmod.register.SoundRegister;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SpawnLivingEntityUtils {

    private static Random random = new Random();

    public static CopyOnWriteArrayList<EntityType> canSpawnEntityType = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<EntityType> canSpawnHostileEntityType = new CopyOnWriteArrayList<>();

    //    public static CopyOnWriteArrayList<Entity> liveEntity = new CopyOnWriteArrayList<>();
    public static List<Entity> spawn(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos) {
        return spawnTimes(list, world, pos, 1, null);
    }


    public static List<Entity> spawnHostileByPlayer(World world, BlockPos pos, UUID playerUuid) {
        return spawnByPlayer(canSpawnHostileEntityType, world, pos, playerUuid);
    }

    public static List<Entity> spawnAllByPlayer(World world, BlockPos pos, UUID playerUuid) {
        return spawnByPlayer(canSpawnEntityType, world, pos, playerUuid);
    }

    public static List<Entity> spawnHostileByPlayerTimes(World world, BlockPos pos, UUID playerUuid, int times) {
        return spawnByPlayerTimes(canSpawnHostileEntityType, world, pos, playerUuid, times);
    }

    public static List<Entity> spawnAllByPlayerTimes(World world, BlockPos pos, UUID playerUuid, int times) {
        return spawnByPlayerTimes(canSpawnEntityType, world, pos, playerUuid, times);
    }


    public static List<Entity> spawnByPlayer(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos, UUID playerUuid) {
        return spawnTimes(list, world, pos, 1, playerUuid);
    }

    public static List<Entity> spawnByPlayerTimes(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos, UUID playerUuid, int times) {
        return spawnTimes(list, world, pos, times, playerUuid);
    }

    public static List<Entity> spawnTimes(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos, int times,  UUID playerUuid) {
        initScareSelfEntityType();
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            Entity entity = trySpawn(list, random.nextInt(list.size()), world, playerUuid);
            if (entity != null) {
                //entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                //world.spawnEntity(entity);
                entities.add(entity);
            }
        }
        return entities;
    }

    private static Entity trySpawn(List<EntityType> entities, int random, World world,  UUID playerUuid) {
        int times = entities.size();
        int time = 0;
        while (time < times) {
            EntityType entityType = entities.get(random);
            Entity entity = entityType.create(world);
            if (entity == null || !(entity instanceof EntityModI)) {
                random++;
                if (random >= entities.size()) {
                    random = 0;
                }
            } else {
                ((EntityModI)entity).setByPlayer(playerUuid);
                return entity;
            }
            time++;
        }
        return null;
    }

    public static List<Entity> spawnScareSelf(CopyOnWriteArrayList<EntityType> list, World world, BlockPos pos) {
        initScareSelfEntityType();
        List<Entity> entities = new ArrayList<>();
        int target = random.nextInt(list.size());
        int index = 0;
        for (EntityType entityType : list) {
            if (index == target) {
                Entity entity = entityType.create(world);
                if (entity == null) {
                    index++;
                    continue;
                }
                entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                world.spawnEntity(entity);
                if (SoundRegister.SCARE_SELF_SOUND_PLAY.get()) {
                    entity.playSound(SoundRegister.SCARE_SELF_SOUND, 1, 1);
                }
                entities.add(entity);
                break;
            }
            index++;
        }
        return entities;
    }

    private static void initScareSelfEntityType() {
        if (!canSpawnEntityType.isEmpty()) {
            return;
        }
        //System.out.println("init spawnScareSelfEntityType");
        Field[] declaredFields = EntityType.class.getDeclaredFields();

        for (Field f : declaredFields) {
            try {
                if (Modifier.isStatic(f.getModifiers())) {
                    if (f.getType() == EntityType.class) {
                        String typeName = f.getGenericType().getTypeName();
                        //System.out.println("put all :: " + typeName);
                        canSpawnEntityType.add((EntityType) f.get(null));
                        String substring = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                        if (Class.forName(substring).getSuperclass() == HostileEntity.class) {
                            //System.out.println("put hostile :: " + typeName);
                            canSpawnHostileEntityType.add((EntityType) f.get(null));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                //System.out.println(e.getMessage());
            } catch (IllegalAccessException e) {
                //System.out.println(e.getMessage());
            }
        }
    }
}
