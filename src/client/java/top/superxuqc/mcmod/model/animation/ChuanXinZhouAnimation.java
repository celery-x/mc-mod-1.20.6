package top.superxuqc.mcmod.model.animation;// Save this class in your mod and generate all required imports

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 4.12.1
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class ChuanXinZhouAnimation {

	public static final Animation ROOSTING = Animation.Builder.create(0.5F)
			.looping()
			.addBoneAnimation(
					"head",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(180.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"head",
					new Transformation(
							Transformation.Targets.TRANSLATE, new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"body",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(180.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"body",
					new Transformation(
							Transformation.Targets.TRANSLATE, new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"feet",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"right_wing",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"right_wing",
					new Transformation(
							Transformation.Targets.TRANSLATE, new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"right_wing_tip",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -120.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"left_wing",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 10.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"left_wing",
					new Transformation(
							Transformation.Targets.TRANSLATE, new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.addBoneAnimation(
					"left_wing_tip",
					new Transformation(
							Transformation.Targets.ROTATE, new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 120.0F, 0.0F), Transformation.Interpolations.LINEAR)
					)
			)
			.build();
	public static final Animation animation = Animation.Builder.create(2.0F).looping()
		.addBoneAnimation("canAni", new Transformation(Transformation.Targets.TRANSLATE,
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
}