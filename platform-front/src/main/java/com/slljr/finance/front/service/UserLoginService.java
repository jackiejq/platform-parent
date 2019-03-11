package com.slljr.finance.front.service;

import com.google.zxing.WriterException;
import com.slljr.finance.common.constants.Constant;
import com.slljr.finance.common.enums.DataStatusEnum;
import com.slljr.finance.common.exception.InterfaceException;
import com.slljr.finance.common.pojo.model.UserAccount;
import com.slljr.finance.common.pojo.model.UserBasic;
import com.slljr.finance.common.pojo.model.UserOauth;
import com.slljr.finance.common.utils.AESUtil;
import com.slljr.finance.common.utils.MathUtils;
import com.slljr.finance.common.utils.MsgEnum;
import com.slljr.finance.common.utils.QRCodeUtil;
import com.slljr.finance.common.utils.oss.AliyunOSSUtil;
import com.slljr.finance.common.utils.oss.OSSResult;
import com.slljr.finance.front.mapper.UserAccountMapper;
import com.slljr.finance.front.mapper.UserBasicMapper;
import com.slljr.finance.front.mapper.UserOauthMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
public class UserLoginService {

    private static final Logger log = LogManager.getLogger();

    @Resource
    UserOauthMapper userOauthMapper;
    @Resource
    UserBasicMapper userBasicMapper;
    @Resource
    UserAccountMapper userAccountMapper;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Value("${app.password.security.key}")
    private String passwordKey;

    /**
     * 邀请好友分享链接
     */
    @Value("${app.shareqr.url}")
    private String shareQrUrl;

    /**
     * 第三方应用登录
     *
     * @param userOauth
     * @return
     */
    public UserBasic oauthLogin(UserOauth userOauth) {
        UserOauth temp = userOauthMapper.selectByOpenIdAndTarget(userOauth.getOpenId(), userOauth.getTarget());
        if (temp == null) {
            //走新建用户流程
            //创建用户基础数据
            UserBasic userBasic = addUserBasic(null, userOauth.getNickName(), userOauth.getHeadImg());

            //创建用户账户数据
            addUserAccount(userBasic.getId());

            //创建Oauth数据
            userOauth.setUid(userBasic.getId());
            userOauth.setCreateTime(new Date());
            userOauth.setUpdateTime(new Date());
            userOauthMapper.insertSelective(userOauth);

            return userBasic;
        } else {
            UserBasic uptUser = new UserBasic();
            uptUser.setId(temp.getUid());
            uptUser.setNickName(userOauth.getNickName());
            uptUser.setHeadImg(userOauth.getHeadImg());
            userBasicMapper.updateByPrimaryKeySelective(uptUser);

            return userBasicMapper.selectByPrimaryKey(temp.getUid());
        }
    }

    /**
     * 短信登录
     *
     * @param phone
     * @return
     */
    public UserBasic smsLogin(String phone) {
        UserBasic userBasic = userBasicMapper.selectByPhone(phone);
        if (userBasic == null) {
            //走新建用户流程
            //创建用户基础数据
            userBasic = addUserBasic(phone, null, null);

            //创建用户账户数据
            addUserAccount(userBasic.getId());
        }
        return userBasic;
    }

    /**
     * 帐号密码登录
     *
     * @param phone
     * @param password
     * @return
     */
    public UserBasic authLogin(String phone, String password) {
        //加密后密码
    	String ecodes = AESUtil.ecodes(password, passwordKey);
    	UserBasic userBasic = userBasicMapper.selectByPhoneAndPassword(phone, ecodes);
    	return userBasic;   	
    }

    private UserBasic addUserBasic(String phone, String nickName, String headImg) {
        if (StringUtils.isBlank(nickName)){
            nickName = "优卡" + (StringUtils.isNotBlank(phone)?
                    StringUtils.substring(phone, 6, 11) : MathUtils.randomBetween(10000, 99999));
        }

        UserBasic userBasic = new UserBasic();
        userBasic.setPhone(phone);
        userBasic.setNickName(nickName);
        userBasic.setHeadImg(headImg);
        userBasic.setStatus(DataStatusEnum.INVALID.getKey());
        userBasic.setGrade(1);
        userBasic.setIntegral(0);
        userBasic.setForumStatus(DataStatusEnum.INVALID.getKey());
        userBasicMapper.insertSelective(userBasic);

        return userBasic;
    }

    /**
     * 用户注册带邀请人
     *
     * @author uncle.quentin
     * @date   2019/1/14 10:07
     * @param   phone
     * @param   refererUid
     * @return com.slljr.finance.common.pojo.model.UserBasic
     * @version 1.0
     */
    private UserBasic addUserBasicWithRefererUid(String phone, int refererUid) {
        UserBasic userBasic = new UserBasic();
        userBasic.setPhone(phone);
        userBasic.setRefererUid(refererUid);
        userBasic.setStatus(DataStatusEnum.INVALID.getKey());
        userBasic.setGrade(1);
        userBasic.setIntegral(0);
        userBasic.setForumStatus(DataStatusEnum.INVALID.getKey());
        userBasicMapper.insertSelective(userBasic);

        return userBasic;
    }

    private UserAccount addUserAccount(int uid) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUid(uid);
        userAccount.setStatus(0);
        userAccount.setCashBalance(0.0);
        userAccount.setIntegralBalance(0.0);
        userAccount.setCreateTime(new Date());
        userAccount.setUpdateTime(new Date());
        userAccountMapper.insert(userAccount);
        return userAccount;
    }


    /**
     * 用户注册
     *
     * @param phone
     * @return boolean
     * @author uncle.quentin
     * @date 2019/1/13 13:17
     * @version 1.0
     */
    public void userRegister(String phone, int refererUid) throws InterfaceException {
        UserBasic userBasic = userBasicMapper.selectByPhone(phone);
        if (userBasic == null) {
            //创建用户基础数据
            userBasic = addUserBasicWithRefererUid(phone, refererUid);
            //创建用户账户数据
            addUserAccount(userBasic.getId());
        } else {
            throw new InterfaceException(MsgEnum.USER_EXISTED);
        }
    }

    /**
     * 生成二维码→上传至阿里云→设置参数userBasic对象shareQrUrl
     *
     * @param userBasic 用户信息
     * @return UserBasic
     * @author uncle.quentin
     * @date 2019/1/13 18:11
     * @version 1.0
     */
    public UserBasic generateQrCode(UserBasic userBasic) throws IOException {
        UserBasic result = new UserBasic();
        //拼接生成邀请好友链接
        String data = getShareUrl(userBasic.getId());
        try {
            BufferedImage bufferedImage = QRCodeUtil.createQrCodeWithCustomizeMargin(data,5);
            //BufferedImage转InputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            //拼接文件名
            String fileName = Constant.SHARE_QR_FILE_NAME_PREFIX + userBasic.getId() + "." + QRCodeUtil.FORMAT;
            //InputStream转换为MultipartFile
            MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, ContentType.APPLICATION_OCTET_STREAM.toString(), is);
            //上传至阿里云
            OSSResult ossResult = aliyunOSSUtil.fileUpload(multipartFile);
            if (null != ossResult) {
                if (StringUtils.isNotEmpty(ossResult.getFileUrl())) {
                    result.setShareQrUrl(ossResult.getFileUrl());
                    result.setId(userBasic.getId());
                }else {
                    log.error("UserLoginService.generateQrCode→上传图片异常：{}", ossResult.getMsg());
                }
            } else {
                log.error("UserLoginService.generateQrCode→上传图片异常");
            }
        } catch (WriterException e) {
            log.error("UserLoginService.generateQrCode→生成二维码异常：", e);
        }

        return result;
    }

    /**
     * 获取分享链接
     * @param uid
     * @return
     */
    public String getShareUrl(int uid){
        //拼接生成邀请好友链接
        StringBuffer sb = new StringBuffer();
        sb.append(shareQrUrl);
        sb.append("?uid=");
        sb.append(uid);
        return sb.toString();
    }

    /**
     * 更新用户分享二维码链接
     *
     * @author uncle.quentin
     * @date   2019/1/13 18:29
     * @param   userBasic
     * @return void
     * @version 1.0
     */
    public UserBasic updateUserShareQr(UserBasic userBasic){
        //生成二维码，修改保存链接shareQrUrl
        try {
            UserBasic updateUser = generateQrCode(userBasic);
            if (null != updateUser) {
                userBasicMapper.updateByPrimaryKeySelective(updateUser);
            }

            return updateUser;
        } catch (IOException e) {
            log.error("生成二维码异常：", e);
        }

        return null;
    }

}
