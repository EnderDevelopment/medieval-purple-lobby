package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private EconomyManager economy;
    private ShopManager shop;
    private AuctionManager auction;
    private HomeManager home;
    private WarpManager warp;
    private KitManager kit;
    private TeleportManager teleport;
    private LobbyManager lobby;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        economy = new EconomyManager(this);
        shop = new ShopManager(this);
        auction = new AuctionManager(this);
        home = new HomeManager(this);
        warp = new WarpManager(this);
        kit = new KitManager(this);
        teleport = new TeleportManager(this);
        lobby = new LobbyManager(this);

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new AuctionListener(this), this);

        getCommand("balance").setExecutor(new EconomyCommands(this));
        getCommand("pay").setExecutor(new EconomyCommands(this));
        getCommand("eco").setExecutor(new EconomyCommands(this));
        getCommand("shop").setExecutor(new ShopCommands(this));
        getCommand("auction").setExecutor(new AuctionCommands(this));
        getCommand("bid").setExecutor(new AuctionCommands(this));
        getCommand("bounty").setExecutor(new BountyCommands(this));
        getCommand("sethome").setExecutor(new HomeCommands(this));
        getCommand("home").setExecutor(new HomeCommands(this));
        getCommand("homes").setExecutor(new HomeCommands(this));
        getCommand("delhome").setExecutor(new HomeCommands(this));
        getCommand("rtp").setExecutor(new RTPCommand(this));
        getCommand("spawn").setExecutor(new LobbyCommands(this));
        getCommand("lobby").setExecutor(new LobbyCommands(this));
        getCommand("accepthub").setExecutor(new LobbyCommands(this));
        getCommand("warp").setExecutor(new WarpCommands(this));
        getCommand("warps").setExecutor(new WarpCommands(this));
        getCommand("setwarp").setExecutor(new WarpCommands(this));
        getCommand("delwarp").setExecutor(new WarpCommands(this));
        getCommand("kits").setExecutor(new KitCommands(this));
        getCommand("kit").setExecutor(new KitCommands(this));
        getCommand("tpa").setExecutor(new TeleportCommands(this));
        getCommand("tpahere").setExecutor(new TeleportCommands(this));
        getCommand("tpaccept").setExecutor(new TeleportCommands(this));
        getCommand("tpdeny").setExecutor(new TeleportCommands(this));
        getCommand("msg").setExecutor(new SocialCommands(this));
        getCommand("reply").setExecutor(new SocialCommands(this));
        getCommand("fly").setExecutor(new AdminCommands(this));
        getCommand("god").setExecutor(new AdminCommands(this));
        getCommand("heal").setExecutor(new AdminCommands(this));
        getCommand("feed").setExecutor(new AdminCommands(this));
        getCommand("gamemode").setExecutor(new AdminCommands(this));

        getLogger().info("EnderMedievalLobby enabled!");
    }

    @Override
    public void onDisable() {
        economy.save();
        home.saveAll();
        warp.saveAll();
        kit.saveAll();
        getLogger().info("EnderMedievalLobby disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public EconomyManager getEconomy() { return economy; }
    public ShopManager getShop() { return shop; }
    public AuctionManager getAuction() { return auction; }
    public HomeManager getHome() { return home; }
    public WarpManager getWarp() { return warp; }
    public KitManager getKit() { return kit; }
    public TeleportManager getTeleport() { return teleport; }
    public LobbyManager getLobby() { return lobby; }
}
