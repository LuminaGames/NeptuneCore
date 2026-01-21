package lol.vedant.neptunecore.commands.staff;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import lol.vedant.core.utils.CommonUtil;
import lol.vedant.neptunecore.NeptuneCore;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.serialize.SerializationException;

public class MaintenanceCommand {

    public static BrigadierCommand createCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> maintenanceCommand = BrigadierCommand
                .literalArgumentBuilder("maintenance")
                .requires(source -> source.hasPermission("neptune.command.maintenance"))
                .then(
                        BrigadierCommand.requiredArgumentBuilder("toggle", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("enable");
                                    builder.suggest("disable");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    CommandSource source = ctx.getSource();
                                    String toggle = ctx.getArgument("toggle", String.class);

                                    if (toggle.equalsIgnoreCase("enable")) {
                                        NeptuneCore.getInstance().getConfig().set("maintenance.enabled", true);
                                        source.sendMessage(CommonUtil.mm("<green>Maintenance has now been enabled."));
                                    } else if (toggle.equalsIgnoreCase("disable")) {
                                        NeptuneCore.getInstance().getConfig().set("maintenance.enabled", false);
                                        source.sendMessage(CommonUtil.mm("<red>Maintenance has now been disabled."));
                                    } else {
                                        source.sendMessage(CommonUtil.mm("Usage: /maintenance <enable|disable>"));
                                        return 0;
                                    }
                                    NeptuneCore.getInstance().saveConfig();

                                    return Command.SINGLE_SUCCESS;
                                })
                                .build()
                )
                .build();

        return new BrigadierCommand(maintenanceCommand);
    }
}