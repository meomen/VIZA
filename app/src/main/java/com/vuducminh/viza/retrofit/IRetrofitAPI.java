package com.vuducminh.viza.retrofit;

import com.vuducminh.viza.models.BalanceRequest;
import com.vuducminh.viza.models.BannerRequest;
import com.vuducminh.viza.models.CardRequest;
import com.vuducminh.viza.models.FullNameRequest;
import com.vuducminh.viza.models.MoneyRequest;
import com.vuducminh.viza.models.NewsRequest;
import com.vuducminh.viza.models.OtherRequest;
import com.vuducminh.viza.models.PageRequest;
import com.vuducminh.viza.models.PaySaveRequest;
import com.vuducminh.viza.models.TransactionRequest;
import com.vuducminh.viza.models.UserRequest;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Linh Lee on 6/18/2017.
 */
public interface IRetrofitAPI {

    //Đăng nhập
    @POST("/rest/api/LoginTcsr")
    Call<UserRequest> login(@Body HashMap<String, Object> body);

    //Kiểm tra mã đăng nhập ODP
    @POST("/rest/api/checkODP")
    Call<OtherRequest> checkODP(@Header("token") String token, @Body HashMap<String, Object> body);

    //Gửi lại mã đăng nhập ODP
    @POST("/rest/api/resentODP")
    Call<OtherRequest> resendODP(@Header("token") String token);

    //Lấy thông tin khách hàng
    @POST("/rest/api/getUserInfo")
    Call<UserRequest> getUserInfo(@Header("token") String token);

    //Lựa chọn chế độ ODP hoặc không chọn
    @POST("/rest/api/chooseODP")
    Call<OtherRequest> chooseODP(@Header("token") String token, @Body HashMap<String, Object> body);

    //Đổi mật khẩu 1
    @POST("/rest/api/changeMK1")
    Call<OtherRequest> changePass1(@Header("token") String token, @Body HashMap<String, Object> body);

    //Đổi mật khẩu 2
    @POST("/rest/api/changeMK2")
    Call<OtherRequest> changePass2(@Header("token") String token, @Body HashMap<String, Object> body);

    //Đổi thông tin cá nhân
    @POST("/rest/api/changeProfile")
    Call<UserRequest> changeProfile(@Header("token") String token, @Body HashMap<String, Object> body);

    //Lấy số dư hiện tại của khách hàng
    @POST("/rest/api/getBalance")
    Call<BalanceRequest> getBalance(@Header("token") String token);

    //Nạp thẻ cào thành tiền
    @POST("/rest/api/cardCharge")
    Call<OtherRequest> cardCharge(@Header("token") String token, @Body HashMap<String, Object> body);

    //Chuyển tiền cho tài khoản khác
    @POST("/rest/api/transferTcsr")
    Call<OtherRequest> transferMoney(@Header("token") String token, @Body HashMap<String, Object> body);

    //Mua thẻ cào, thẻ game
    @POST("/rest/api/buyCard")
    Call<OtherRequest> buyCard(@Header("token") String token, @Body HashMap<String, Object> body);

    //Nạp trực tiếp tiền cho di động bằng tiền trên thecaosieure
    @POST("/rest/api/topupMobile")
    Call<OtherRequest> topupMobile(@Header("token") String token, @Body HashMap<String, Object> body);

    //Lấy thông tin tên ngân hàng hoặc tên loại thẻ cho các chức năng rút tiền và các chức năng mua thẻ game điện thoại
    @POST("/rest/api/getCardInfo")
    Call<CardRequest> getCardInfo(@Body HashMap<String, Object> body);

    //Lấy thông tin chiết khấu nạp thẻ vào ví
    @POST("/rest/api/getCardDPT")
    Call<CardRequest> getCardDPT();

    //Lấy DS số ATM đã được lưu trên hệ thống của khách hàng
    @POST("/rest/api/getPaysave")
    Call<PaySaveRequest> getPaySave(@Header("token") String token, @Body HashMap<String, Object> body);

    //Rút tiền về số tài khoản ngân hàng
    @POST("/rest/api/postAccountbank")
    Call<OtherRequest> postAccountBank(@Header("token") String token, @Body HashMap<String, Object> body);

    //Rút tiền về số ATM ngân hàng
    @POST("/rest/api/postATMBank")
    Call<OtherRequest> postATMBank(@Header("token") String token, @Body HashMap<String, Object> body);

    //Rút tiền theo số chứng minh thư và địa chỉ cmt
    @POST("/rest/api/postCMTdraw")
    Call<OtherRequest> postCMTdraw(@Header("token") String token, @Body HashMap<String, Object> body);

    //Lấy nội dung hiển thị theo tên chức năng
    @POST("/rest/api/getContentbyname")
    Call<PageRequest> getContentByName(@Body HashMap<String, Object> body);

    //Lấy Danh sách lịch sử giao dịch của khách hàng
    @POST("/rest/api/getAllTransaction")
    Call<TransactionRequest> getAllTransaction(@Header("token") String token, @Body HashMap<String, Object> body);

    //Đăng xuất
    @POST("/rest/api/LogoutTcsr")
    Call<OtherRequest> logout(@Header("token") String token);

    //Thanh toán hợp đồng trả góp
    @POST("/rest/api/postSaleHD")
    Call<OtherRequest> postSaleHD(@Header("token") String token, @Body HashMap<String, Object> body);

    //Lấy ảnh top banner ở trang chủ
    @POST("/rest/api/getBanner")
    Call<BannerRequest> getBanner();

    //Lấy danh sách tin tức
    @POST("/rest/api/getArticle")
    Call<NewsRequest> getArticle();

    //Lấy fullname theo tên tài khoản
    @POST("/rest/api/getFullnameUser")
    Call<FullNameRequest> getFullName(@Body HashMap<String, Object> body);

    //Lấy số tiền phải thanh toán của khách khi mua thẻ
    @POST("/rest/api/getRealAmountBuyCard")
    Call<MoneyRequest> getRealAmountBuyCard(@Header("token") String token, @Body HashMap<String, Object> body);

    //Lấy số tiền phải thanh toán của khách khi nạp topup
    @POST("/rest/api/getRealAmountTopup")
    Call<MoneyRequest> getRealAmountTopup(@Header("token") String token, @Body HashMap<String, Object> body);
}

