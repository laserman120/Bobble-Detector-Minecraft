package net.glad0s.bobberdetector.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.client.renderer.RenderStateShard;

import java.util.OptionalDouble;

public class RenderUtils {
    public static void renderShape(PoseStack poseStack, VoxelShape shape, float red, float green, float blue, float alpha, boolean depthTest){
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = bufferSource.getBuffer(LINES);
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            Vec3 normal = new Vec3(x2 - x1, y2 - y1, z2 - z1);
            normal.normalize();
            builder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(red, green, blue, alpha).normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
            builder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(red, green, blue, alpha).normal(matrix3f, (float)normal.x, (float)normal.y, (float)normal.z).endVertex();
        });
        bufferSource.endBatch(RenderTypes.getOutlineSolid());
    }

    private static final RenderType LINES = RenderType.create("bobberdetector:lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 128, true, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE).createCompositeState(false)
    );
}
