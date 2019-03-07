package me.robnoo02.plotreviewplugin.guis;

import java.util.LinkedList;

import org.bukkit.entity.Player;

import me.robnoo02.plotreviewplugin.guis.GuiUtil.Gui;
import me.robnoo02.plotreviewplugin.guis.GuiUtil.GuiItem;
import me.robnoo02.plotreviewplugin.review.Review;
import me.robnoo02.plotreviewplugin.review.ReviewManager;
import me.robnoo02.plotreviewplugin.review.ReviewStaff;
import me.robnoo02.plotreviewplugin.review.Reviewee;

public class GuiFactory {

	public static Gui reviewGui(final ReviewStaff staff) {
		return new Gui.Builder((Player) staff.getOfflinePlayer())
				.fillSlots(0, 5, getReviewHeads((LinkedList<Review>) ReviewManager.getInstance().getReviews().values()))
				.build();
	}

	private static GuiItem[] getReviewHeads(LinkedList<Review> list) {
		GuiItem[] itemArray = new GuiItem[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Reviewee rev = list.get(i).getReviewee();
			itemArray[i] = getReviewHead(rev, i);
		}
		return itemArray;
	}

	private static GuiItem getReviewHead(Reviewee rev, int queue) {
		return new GuiItem.Builder().playerSkull(rev.getName()).name(rev.getCurrentRank() + rev.getName()).build();
	}

}
