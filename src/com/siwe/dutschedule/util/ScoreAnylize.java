package com.siwe.dutschedule.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.siwe.dutschedule.model.Score;

public class ScoreAnylize {

	ArrayList<Score> alllist = null;
	float creditAll = 0.0f;
	float creditNeed = 0.0f;
	float averAll = 0.0f;
	float averNeed = 0.0f;
	float jidianAll = 0.0f;
	float jidianNeed = 0.0f;

	public ScoreAnylize(ArrayList<Score> list) {
		this.alllist = list;
		float jidianAllTemp = 0.0f;
		float jidianNeedTemp = 0.0f;
		float scoreNeedTemp = 0.0f;
		float scoreAllTemp = 0.0f;

		for (Score item : list) {
			float credit = 0.0f;
			float score = 0.0f;
			try {
				credit = Float.parseFloat(item.getCredit());
				score = Float.parseFloat(item.getScore());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (item.getType().equalsIgnoreCase("必修")) {
				creditNeed += credit;
				scoreNeedTemp += score * credit;
				jidianNeedTemp += 0.1 * (score - 50) * credit;
			}
			creditAll += credit;
			scoreAllTemp += score * credit;
			jidianAllTemp += 0.1 * (score - 50) * credit;
		}

		averAll = scoreAllTemp / creditAll;
		averNeed = scoreNeedTemp / creditNeed;

		jidianAll = jidianAllTemp / creditAll;
		jidianNeed = jidianNeedTemp / creditNeed;

	}

	public String getCreditAll() {
		return format(creditAll);
	}

	public String getCreditNeed() {
		return format(creditNeed);
	}

	public String getAverAll() {
		return format(averAll);
	}

	public String getAverNeed() {
		return format(averNeed);
	}

	public String getJidianAll() {
		return format(jidianAll);
	}

	public String getJidianNeed() {
		return format(jidianNeed);
	}

	private String format(float data) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(data);
	}

}
