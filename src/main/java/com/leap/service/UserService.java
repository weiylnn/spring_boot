package com.leap.service;

import com.leap.dao.UserDao;
import com.leap.handle.exception.base.ExceptionEnum;
import com.leap.model.User;
import com.leap.service.connect.IUserServer;
import com.leap.util.IsEmpty;
import com.leap.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author : ylwei
 * @time : 2017/9/5
 * @description :
 */
@Service
public class UserService implements IUserServer {

  private final UserDao userDao;

  @Autowired
  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public boolean delete(String mobile) {
    User user = userDao.findByMobile(mobile);
    user.setHistory(
        (IsEmpty.string(user.getHistory()) ? "" : user.getHistory()) + user.getMobile() + "&");
    user.setMobile("11111111111");
    user.setId("&" + user.getId());
    user.setEnabled(false);
    user.setNormal(false);
    userDao.delete(user);
    return true;
  }

  @Override
  public User update(User user) {
    ValidUtil.valid(user.getId(), ExceptionEnum.DATA_EMPTY_ID);
    User temp = userDao.get(user.getId());
    ValidUtil.validDB(user.getVersion(), temp.getVersion());
    temp.setVersion(user.getVersion() + 1);
    temp.setLastModified(new Date());
    temp.setName(user.getName());
    temp.setShortName(user.getShortName());
    temp.setEmail(user.getEmail());
    temp.setIdCard(user.getIdCard());
    temp.setBirth(user.getBirth());
    temp.setRemark(user.getRemark());
    temp.setEducation(user.getEducation());
    temp.setPhotoName(user.getPhotoName());
    temp.setPhotoUrl(user.getPhotoUrl());
    return userDao.update(temp);
  }

  @Override
  public User get(String id) {
    return userDao.get(id);
  }

  @Override
  public List<User> query() {
    return userDao.query();
  }

  @Override
  public void save(User user) {
    user.setCreated(new Date());
    user.setLastModified(new Date());
    user.setVersion(1);
    user.setEnabled(true);
    user.setNormal(true);
    user.setName(user.getMobile());
    userDao.save(user);
  }

  @Override
  public void mobileReset(String mobile, String oldMobile) {
    User user = userDao.findByMobile(oldMobile);
    user.setLastModified(new Date());
    user.setHistory(
        (IsEmpty.string(user.getHistory()) ? "" : user.getHistory()) + user.getMobile() + "&");
    user.setMobile(mobile);
    user.setVersion(user.getVersion() + 1);
    userDao.update(user);
  }
}
