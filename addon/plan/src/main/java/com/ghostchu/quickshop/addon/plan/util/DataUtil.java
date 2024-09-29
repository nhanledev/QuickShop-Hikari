package com.ghostchu.quickshop.addon.plan.util;

import com.ghostchu.quickshop.addon.plan.Main;
import com.ghostchu.quickshop.api.database.ShopMetricRecord;
import com.ghostchu.quickshop.api.database.bean.DataRecord;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.common.util.CommonUtil;
import com.ghostchu.quickshop.util.Util;
import com.google.common.html.HtmlEscapers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.UUID;

public class DataUtil {

  public final Main main;

  public DataUtil(@NotNull final Main main) {

    this.main = main;
  }

  @NotNull
  public String formatEconomy(@NotNull final ShopMetricRecord record) {

    final Shop shop = main.getQuickShop().getShopManager().getShop(record.getShopId());
    if(shop == null || main.getQuickShop().getEconomy() == null) {
      final DecimalFormat df = new DecimalFormat("#.00");
      return df.format(record.getTotal());
    }
    return main.getQuickShop().getEconomy().format(record.getTotal(), shop.getLocation().getWorld(), shop.getCurrency());
  }

  @NotNull
  public String getItemName(@NotNull final DataRecord dataRecord) {

    final ItemStack stack;
    try {
      stack = Util.deserialize(dataRecord.getItem());
    } catch(InvalidConfigurationException e) {
      return "[Failed to deserialize]";
    }
    if(stack == null) {
      return "[Failed to deserialize]";
    }
    String name = CommonUtil.prettifyText(stack.getType().name());
    if(stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
      name = stack.getItemMeta().getDisplayName();
    }
    return HtmlEscapers.htmlEscaper().escape(name);
  }

  @NotNull
  public String getItemName(@NotNull final ItemStack stack) {

    String name = CommonUtil.prettifyText(stack.getType().name());
    if(stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName()) {
      name = stack.getItemMeta().getDisplayName();
    }
    return HtmlEscapers.htmlEscaper().escape(name);
  }

  @NotNull
  public String getShopName(@NotNull final ShopMetricRecord record, @NotNull final DataRecord dataRecord) {

    final StringBuilder nameBuilder = new StringBuilder();
    final Shop shop = main.getQuickShop().getShopManager().getShop(record.getShopId());
    if(shop == null) {
      nameBuilder.append("[Deleted] ");
    }
    final String shopName = dataRecord.getName();
    if(shopName != null) {
      nameBuilder.append(ChatColor.stripColor(shopName));
    } else {
      if(shop != null) {
        final Location location = shop.getLocation();
        final String template = "%s %s,%s,%s";
        nameBuilder.append(String.format(template, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
      } else {
        nameBuilder.append("N/A");
      }
    }
    return HtmlEscapers.htmlEscaper().escape(nameBuilder.toString());
  }

  @NotNull
  public String loc2String(@NotNull final Location location) {

    final String template = "%s %s,%s,%s";
    return String.format(template, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  @NotNull
  public String getPlayerName(@NotNull final UUID uuid) {

    if(CommonUtil.getNilUniqueId().equals(uuid)) {
      return "[Server]";
    }
    final String name = main.getQuickShop().getPlayerFinder().uuid2Name(uuid);
    if(name == null || name.isEmpty()) {
      return uuid.toString();
    }
    return HtmlEscapers.htmlEscaper().escape(name);
  }
}
