package df;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(plasticcraft.MODID)
public class plasticcraft {
    public static final String MODID = "plasticcraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    // 创建注册器
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 注册塑料方块 - 颜色请放在名称后面
    public static final DeferredBlock<Block> PLASTIC_BLOCK_GREEN = BLOCKS.register("plastic_block_green",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> PLASTIC_BLOCK_WHITE = BLOCKS.register("plastic_block_white",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.SNOW)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> PLASTIC_BLOCK_RED = BLOCKS.register("plastic_block_red",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(2.0f)
                    .requiresCorrectToolForDrops()));

    // 注册塑料方块的物品形式
    public static final DeferredItem<BlockItem> PLASTIC_BLOCK_GREEN_ITEM = ITEMS.register("plastic_block_green",
            () -> new BlockItem(PLASTIC_BLOCK_GREEN.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> PLASTIC_BLOCK_WHITE_ITEM = ITEMS.register("plastic_block_white",
            () -> new BlockItem(PLASTIC_BLOCK_WHITE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> PLASTIC_BLOCK_RED_ITEM = ITEMS.register("plastic_block_red",
            () -> new BlockItem(PLASTIC_BLOCK_RED.get(), new Item.Properties()));
    // 注册创意标签页
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PLASTIC_TAB = CREATIVE_MODE_TABS.register("plastic_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.plasticcraft.plastic_tab"))
                    .icon(() -> PLASTIC_BLOCK_GREEN_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(PLASTIC_BLOCK_GREEN_ITEM.get());
                        output.accept(PLASTIC_BLOCK_WHITE_ITEM.get());
                        output.accept(PLASTIC_BLOCK_RED_ITEM.get());
                    }).build());

    public plasticcraft(IEventBus modEventBus, ModContainer modContainer) {
        // 注册到事件总线
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // 注册事件监听器
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("塑料工艺模组初始化完成!");

        // 配置日志输出
        if (Config.logDirtBlock) {
            LOGGER.info("配置示例: 泥土方块 >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
    }

    // 将方块添加到原版创意标签页
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(PLASTIC_BLOCK_GREEN_ITEM);
            event.accept(PLASTIC_BLOCK_WHITE_ITEM);
            event.accept(PLASTIC_BLOCK_RED_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("塑料工艺服务器端初始化");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("塑料工艺客户端初始化");
            LOGGER.info("玩家名称 >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}