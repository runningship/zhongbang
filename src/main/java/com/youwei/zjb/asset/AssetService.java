package com.youwei.zjb.asset;

import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.asset.entity.Asset;

/**
 * 资产管理
 */
public class AssetService {
	
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(Asset asset){
		asset.addtime = new Date();
		asset.edittime = new Date();
	}
	
	public void update(Asset asset){
		asset.edittime = new Date();
		dao.saveOrUpdate(asset);
	}
	
	public void delete(Integer id){
		Asset asset = dao.get(Asset.class, id);
		if(asset!=null){
			dao.delete(asset);
		}
	}
}
