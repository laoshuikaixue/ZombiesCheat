package win.cuteguimc.zombieshelper.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.mixin.MinecraftAccessor;
import win.cuteguimc.zombieshelper.mixin.RenderManagerAccessor;
import win.cuteguimc.zombieshelper.utils.RotationUtils;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.awt.*;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class AimBotListener {
    private final Minecraft mc = Minecraft.getMinecraft();
    private float lastRenderPartialTicks = 0;

    public AimBotListener() {
        EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        Entity bestTarget = null;
        double angleDiffToBestTarget = Double.MAX_VALUE;
        float turnSpeedPerTick = ZombiesHelperConfig.turnSpeed;
        float renderPartialTicks = ((MinecraftAccessor) mc).getTimer().renderPartialTicks;
        float diffRenderPartialTicks = renderPartialTicks - lastRenderPartialTicks;
        if (diffRenderPartialTicks < 0) diffRenderPartialTicks = renderPartialTicks;

        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (!Utils.isTarget(entity)) continue;
            Vec3 headPos = entity.getPositionEyes(renderPartialTicks);
            headPos = headPos.addVector(0, 0.1, 0);
            double motionX = entity.motionX;
            double motionY = entity.motionY;
            double motionZ = entity.motionZ;
            if (entity.onGround) {
                motionY = 0;
            }
            float predict = ZombiesHelperConfig.predict;
            Vec3 predictPos = headPos.addVector(motionX * predict, motionY * predict * 0.2, motionZ * predict);

            if (mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionEyes(renderPartialTicks), predictPos, true, true, false) != null) {
                continue;
            }

            double angleDiff = RotationUtils.getAngleDifferenceToPoint(headPos.xCoord, headPos.yCoord, headPos.zCoord);
            if (angleDiff < angleDiffToBestTarget) {
                angleDiffToBestTarget = angleDiff;
                bestTarget = entity;
            }
        }

        if (bestTarget != null && ZombiesHelperConfig.aimBot) {
            Vec3 headPos = bestTarget.getPositionEyes(renderPartialTicks);
            headPos = headPos.addVector(0, 0.1, 0);
            float predict = ZombiesHelperConfig.predict;
            double motionX = bestTarget.motionX;
            double motionY = bestTarget.motionY;
            double motionZ = bestTarget.motionZ;
            if (bestTarget.onGround) {
                motionY = 0;
            }
            Vec3 predictedPos = headPos.addVector(motionX * predict, motionY * predict * 0.2, motionZ * predict);
            
            float[] rotations = RotationUtils.getRotationsToPoint(predictedPos.xCoord, predictedPos.yCoord, predictedPos.zCoord);

            float yawDiff = rotations[0] - mc.thePlayer.prevRotationYaw;
            float pitchDiff = rotations[1] - mc.thePlayer.prevRotationPitch;
            while (Math.abs(yawDiff) > 180.0F) {
                yawDiff = (yawDiff > 0.0F ? yawDiff - 360.0F : yawDiff + 360.0F);
            }
            float tickYaw = Math.min(Math.max(yawDiff, -turnSpeedPerTick), turnSpeedPerTick) * diffRenderPartialTicks;
            float tickPitch = Math.min(Math.max(pitchDiff, -turnSpeedPerTick), turnSpeedPerTick) * diffRenderPartialTicks;
            mc.thePlayer.rotationYaw += tickYaw;
            mc.thePlayer.rotationPitch += tickPitch;
        }

        lastRenderPartialTicks = renderPartialTicks;
    }

    private void drawPredictionVisualization(Vec3 headPos, Vec3 predictedPos, float renderPartialTicks) {
        // 设置渲染状态
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        // 获取渲染偏移量
        double renderPosX = ((RenderManagerAccessor) mc.getRenderManager()).getRenderPosX();
        double renderPosY = ((RenderManagerAccessor) mc.getRenderManager()).getRenderPosY();
        double renderPosZ = ((RenderManagerAccessor) mc.getRenderManager()).getRenderPosZ();

        // 计算相对位置
        double headX = headPos.xCoord - renderPosX;
        double headY = headPos.yCoord - renderPosY;
        double headZ = headPos.zCoord - renderPosZ;

        double predictX = predictedPos.xCoord - renderPosX;
        double predictY = predictedPos.yCoord - renderPosY;
        double predictZ = predictedPos.zCoord - renderPosZ;

        // 绘制实际位置 (红色球体)
        drawSphere(headX, headY, headZ, 0.1, Color.RED);

        // 绘制预测位置 (绿色球体)
        drawSphere(predictX, predictY, predictZ, 0.1, Color.GREEN);

        // 绘制连接线 (黄色线条)
        GL11.glLineWidth(2.0f);
        GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.8f); // 黄色
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(headX, headY, headZ);
        GL11.glVertex3d(predictX, predictY, predictZ);
        GL11.glEnd();

        // 恢复渲染状态
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    /**
     * 绘制一个简单的球体
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @param radius 半径
     * @param color 颜色
     */
    private void drawSphere(double x, double y, double z, double radius, Color color) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        GL11.glColor4f(r, g, b, 0.8f);

        // 绘制一个简单的立方体来表示球体
        GL11.glBegin(GL11.GL_QUADS);
        
        // 前面
        GL11.glVertex3d(-radius, -radius, radius);
        GL11.glVertex3d(radius, -radius, radius);
        GL11.glVertex3d(radius, radius, radius);
        GL11.glVertex3d(-radius, radius, radius);
        
        // 后面
        GL11.glVertex3d(-radius, -radius, -radius);
        GL11.glVertex3d(-radius, radius, -radius);
        GL11.glVertex3d(radius, radius, -radius);
        GL11.glVertex3d(radius, -radius, -radius);
        
        // 上面
        GL11.glVertex3d(-radius, radius, -radius);
        GL11.glVertex3d(-radius, radius, radius);
        GL11.glVertex3d(radius, radius, radius);
        GL11.glVertex3d(radius, radius, -radius);
        
        // 下面
        GL11.glVertex3d(-radius, -radius, -radius);
        GL11.glVertex3d(radius, -radius, -radius);
        GL11.glVertex3d(radius, -radius, radius);
        GL11.glVertex3d(-radius, -radius, radius);
        
        // 右面
        GL11.glVertex3d(radius, -radius, -radius);
        GL11.glVertex3d(radius, radius, -radius);
        GL11.glVertex3d(radius, radius, radius);
        GL11.glVertex3d(radius, -radius, radius);
        
        // 左面
        GL11.glVertex3d(-radius, -radius, -radius);
        GL11.glVertex3d(-radius, -radius, radius);
        GL11.glVertex3d(-radius, radius, radius);
        GL11.glVertex3d(-radius, radius, -radius);
        
        GL11.glEnd();
        GL11.glPopMatrix();
    }
}
