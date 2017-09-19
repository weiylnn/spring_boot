package com.leap.model.convert;

import com.leap.config.MarsConfig;
import com.leap.model.Dialogue;
import com.leap.model.out.OutDialogue;
import com.leap.model.tuling.BChat;
import com.leap.model.tuling.CChat;
import com.leap.model.tuling.News;
import com.leap.util.GsonUtil;
import com.leap.util.IsEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author : ylwei
 * @time : 2017/9/8
 * @description :
 */
public class DialogueConvert {

  // Dialogue convert OutDialogue
  public static OutDialogue DialogueToA(Dialogue dialogue) {
    if (IsEmpty.object(dialogue))
      return null;
    OutDialogue outDialogue = new OutDialogue();
    outDialogue.setId(dialogue.getId());
    outDialogue.setCode(dialogue.getCode());
    outDialogue.setInfo(dialogue.getInfo());
    outDialogue.setLoc(dialogue.getLoc());
    outDialogue.setUserId(dialogue.getUserId());
    outDialogue.setTime(dialogue.getTime());
    outDialogue.setText(dialogue.getText());
    outDialogue.setUrl(dialogue.getUrl());
    outDialogue.setAsk(dialogue.isAsk());
    outDialogue.setList(GsonUtil.parseLst(dialogue.getList(), News.class));
    return outDialogue;
  }

  // List<Dialogue> convert List<OutDialogue>
  public static List<OutDialogue> UserListToA(List<Dialogue> dialogueList) {
    List<OutDialogue> outDialogueList = new ArrayList<>();
    if (IsEmpty.object(dialogueList))
      return null;
    for (Dialogue dialogue : dialogueList) {
      outDialogueList.add(DialogueToA(dialogue));
    }
    return outDialogueList;
  }

  // BChat convert Dialogue
  public static Dialogue BChatToD(BChat chat) {
    if (IsEmpty.object(chat))
      return null;
    Dialogue dialogue = new Dialogue();
    dialogue.setAsk(true);
    dialogue.setId(UUID.randomUUID().toString());
    dialogue.setUserId(chat.getUserid());
    dialogue.setCode(0);
    dialogue.setKey(chat.getKey());
    dialogue.setInfo(chat.getInfo());
    dialogue.setLoc(chat.getLoc());
    dialogue.setTime(chat.getTime());
    dialogue.setNormal(true);
    return dialogue;
  }

  // CChat convert Dialogue
  public static Dialogue CChatToD(CChat chat) {
    if (IsEmpty.object(chat))
      return null;
    Dialogue dialogue = new Dialogue();
    dialogue.setAsk(false);
    dialogue.setId(UUID.randomUUID().toString());
    dialogue.setUserId(chat.getUserId());
    dialogue.setKey(MarsConfig.TU_LING_KEY);
    dialogue.setTime(chat.getTime());
    dialogue.setCode(chat.getCode());
    dialogue.setText(chat.getText());
    dialogue.setUrl(chat.getUrl());
    dialogue.setList(GsonUtil.toJson(chat.getList()));
    dialogue.setNormal(true);
    return dialogue;
  }
}
