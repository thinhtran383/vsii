package space.thinhtran.warehouse.service;

import space.thinhtran.warehouse.dto.request.auth.LoginReq;
import space.thinhtran.warehouse.dto.request.auth.RegisterReq;
import space.thinhtran.warehouse.dto.response.auth.LoginResp;
import space.thinhtran.warehouse.dto.response.auth.RegisterResp;

public interface IAuthService {
    RegisterResp createUser(RegisterReq req);
    LoginResp login(LoginReq req);
}
