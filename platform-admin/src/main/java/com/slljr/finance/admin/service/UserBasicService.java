package com.slljr.finance.admin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.slljr.finance.admin.mapper.UserBasicMapper;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.vo.UserBasicByVO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserBasicService {
    @Resource
    UserBasicMapper userBasicMapper;

    public PageInfo listPtUser(UserBasicByVO userBasic, int pageNum, int pageSize){
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> userBasicMapper.findPtUserByAll(userBasic));
    }

    public PageInfo listAdminUser(UserBasic userBasic, int pageNum, int pageSize){
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> userBasicMapper.findAdminUserByAll(userBasic));
    }

    /**
     * 新增一条用户基础信息
     *
     * @author uncle.quentin
     * @date   2018/12/11 14:33
     * @param   userBasic
     * @return int
     * @version 1.0
     */
    @Transactional(rollbackFor = Exception.class)
    public int addUserBasic(UserBasic userBasic){
        return userBasicMapper.insertSelective(userBasic);
    }

    /**
     * 删除一条基础用户信息
     *
     * @author uncle.quentin
     * @date   2018/12/11 14:34
     * @param   userId
     * @return int
     * @version 1.0
     */
    public int deleteUserBasic(int userId){
        return userBasicMapper.deleteByPrimaryKey(userId);
    }

    /**
     * 修改一条基础用户信息
     *
     * @author uncle.quentin
     * @date   2018/12/11 15:09
     * @param   userBasic
     * @return int
     * @version 1.0
     */
    public int updateUserBasic(UserBasic userBasic) {
        return userBasicMapper.updateByPrimaryKeySelective(userBasic);
    }

    /**
     * 条件查询基础用户信息
     *
     * @author uncle.quentin
     * @date   2018/12/11 16:16
     * @param   userBasic
     * @return java.util.List<com.slljr.finance.common.pojo.model.UserBasic>
     * @version 1.0
     */
    public List<UserBasic> findByCondition(UserBasic userBasic){
        return userBasicMapper.findAdminUserByAll(userBasic);
    }

    /**
     * 根据ID查询用户信息
     *
     * @author uncle.quentin
     * @date   2018/12/12 10:41
     * @param   id
     * @return com.slljr.finance.common.pojo.model.UserBasic
     * @version 1.0
     */
    public UserBasic findUserBasicById(Integer id){
        return userBasicMapper.selectByPrimaryKey(id);
    }
}
