package top.superxuqc.mcmod.block.interfaces;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface WithTag {

    default void setTag(String tag){};

    default String getTag(){
        return "";
    };

    default void setTags(Set<String> tags){};

    default Set<String> getTags(){
        return new HashSet<>();
    };

}
