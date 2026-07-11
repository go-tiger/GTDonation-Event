package dev.gotiger.gTDonationEvent.action.hazard.tnt;

import dev.gotiger.gTDonationEvent.action.DonationAction;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.plugin.java.JavaPlugin;

public class TntAction implements DonationAction {

    private final JavaPlugin plugin;

    public TntAction(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "TNT";
    }

    @Override
    public void execute(Player target, int amount) {
        int fuseTicks = plugin.getConfig().getInt("tnt.fuse-ticks", 80);

        TNTPrimed tnt = target.getWorld().spawn(target.getLocation(), TNTPrimed.class);
        tnt.setFuseTicks(fuseTicks);
        tnt.setSource(target);
    }
}
