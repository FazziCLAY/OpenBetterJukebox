package ru.fazziclay.openbetterjukebox;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientMainMod implements ClientModInitializer {
	public static final String MOD_TAG = "openbetterjukebox";
	public static final Logger LOGGER = LogManager.getLogger(MOD_TAG);
	public static ServerWorld serverWorld;

	@Override
	public void onInitializeClient() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (player == null || world == null || hitResult == null) return ActionResult.PASS;

			BlockState blockState = world.getBlockState(hitResult.getBlockPos());
			Block block = blockState.getBlock();
			BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
			LOGGER.info(String.format("""
							UseBlockCallback:
							 * player: %s\s
							 * world: %s\s
							 * hand: %s
							 * hitResult: %s
							 * blockState: %s
							 * blockEntity: %s
							 * block: %s""",
					player,
					world,
					hand,
					hitResult,
					blockState,
					blockEntity,
					block));

			if (block instanceof JukeboxBlock) {
				if (player.isSneaky()) return ActionResult.PASS;

				if (!world.isClient()) serverWorld = (ServerWorld) world;
				if (serverWorld == null) {
					MinecraftServer server = player.getServer();
					if (server != null) {
						PlayerManager playerManager = server.getPlayerManager();
						ServerPlayerEntity serverPlayer = playerManager.getPlayer(player.getEntityName());
						if (serverPlayer != null) {
							ClientMainMod.serverWorld = serverPlayer.getServerWorld();
						}
					}
				}

				if (world.isClient() && serverWorld != null) {
					MinecraftClient.getInstance().setScreen(new JukeboxScreen());
				}
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});
	}
}
