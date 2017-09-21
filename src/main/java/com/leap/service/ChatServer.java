package com.leap.service;

import com.leap.dao.DialogueDao;
import com.leap.handle.exception.base.BaseException;
import com.leap.model.Dialogue;
import com.leap.model.convert.DialogueConvert;
import com.leap.model.in.network.Response;
import com.leap.model.out.OutDialogue;
import com.leap.model.tuling.BChat;
import com.leap.model.tuling.CChat;
import com.leap.network.HttpServlet;
import com.leap.service.connect.IChatServer;
import com.leap.util.GsonUtil;
import com.leap.util.IsEmpty;
import com.leap.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author : ylwei
 * @time : 2017/9/18
 * @description :
 */
@Service
public class ChatServer implements IChatServer {

  private final DialogueDao dialogueDao;
  private final HttpServlet servlet;

  @Autowired
  public ChatServer(DialogueDao dialogueDao, HttpServlet servlet) {
    this.dialogueDao = dialogueDao;
    this.servlet = servlet;
  }

  @Transactional
  @Override
  public Response chat(BChat chat) throws IOException {
    chat.setTime(IsEmpty.object(chat.getTime()) ? new Date() : chat.getTime());
    dialogueDao.save(DialogueConvert.BChatToD(chat));
    String result = servlet.tuLingQuest(chat);
    CChat cChat = GsonUtil.parse(result, CChat.class);
    cChat.setUserId(chat.getUserid());
    cChat.setTime(new Date());
    Dialogue dialogue = dialogueDao.save(DialogueConvert.CChatToD(cChat));
    return ResultUtil.success(DialogueConvert.DialogueToA(dialogue));
  }

  @Override
  public Response query(String userId) throws BaseException {
    List<Dialogue> dialogueList = dialogueDao.query(userId, true);
    List<OutDialogue> outDialogueList = DialogueConvert.UserListToA(dialogueList);
    return ResultUtil.success(outDialogueList,
        IsEmpty.list(outDialogueList) ? 0 : outDialogueList.size(), true);
  }

  @Transactional
  @Override
  public Response delete(String id) throws BaseException {
    Dialogue dialogue = dialogueDao.get(id);
    dialogueDao.delete(dialogue);
    return ResultUtil.success(true);
  }
}