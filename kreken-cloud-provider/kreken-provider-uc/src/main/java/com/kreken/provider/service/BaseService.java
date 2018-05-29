package com.kreken.provider.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kreken.exception.BusinessException;
import com.kreken.provider.tkmapper.MyMapper;
import com.kreken.util.StringUtil;
import com.mysql.jdbc.StringUtils;
import com.kreken.provider.model.common.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseService<T extends BaseEntity, K extends MyMapper<T>> {

	private static Logger logger = LoggerFactory.getLogger(BaseService.class);

	@Autowired
	protected K mapper;

	protected abstract String getTableName();

	/**
	 * 根据条件查询(不分页)
	 * 
	 * @param t
	 *            默认实例查询条件
	 * @param orderBy
	 *            排序条件
	 * @return
	 */
	public List<T> getAll(T t, String orderBy) {
		return getAll(t, null, orderBy);
	}

	/**
	 * 根据条件查询(不分页)
	 * 
	 * @param t
	 *            默认实例查询条件
	 * @param top
	 *            最多取多少条记录
	 * @param orderBy
	 *            排序条件
	 * @return
	 */
	public List<T> getAll(T t, Integer top, String orderBy) {
		List<T> list = null;

		try {
			// 排序
			if (StringUtils.isNullOrEmpty(orderBy)) {
				orderBy = "id desc";
			}

			PageHelper.orderBy(orderBy);

			if (top != null && top > 0) {
				PageHelper.startPage(1, top, false);
			}

			list = mapper.select(t);
		} catch (Exception ex) {
			logger.error("全部查询多条" + this.getTableName() + "信息失败！", ex);
		}

		return list;
	}

	/**
	 * 根据自由构建条件查询(不分页)
	 * 
	 * @param p
	 * @param orderBy
	 * @return
	 */
	public List<T> getAllExt(Example p, String orderBy) {
		return getAllExt(p, null, orderBy);
	}

	/**
	 * 根据自由构建条件查询(不分页)
	 * 
	 * @param p
	 * @param orderBy
	 * @return
	 */
	public List<T> getAllExt(Example p, Integer top, String orderBy) {

		List<T> list = null;

		try {
			// 排序
			if (StringUtils.isNullOrEmpty(orderBy)) {
				orderBy = "id desc";
			}

			PageHelper.orderBy(orderBy);

			if (top != null && top > 0) {
				PageHelper.startPage(1, top, false);
			}

			list = mapper.selectByExample(p);

		} catch (Exception ex) {
			logger.error("全部查询多条" + this.getTableName() + "信息失败！", ex);
		}

		return list;
	}

	/**
	 * 根据条件分页查询(setPage()，setRows())，默认每页10条记录
	 * 
	 * @param t
	 * @param orderBy
	 * @return
	 * @throws BusinessException
	 */
	public PageInfo<T> getPage(T t, String orderBy) {

		PageInfo<T> pager = null;

		try {
			// 分页，page默认下标从1开始， rows每页返回行数
			if (t.getPage() != null && t.getRows() != null) {
				PageHelper.startPage(t.getPage(), t.getRows());
			} else {
				PageHelper.startPage(1, 10);
			}

			// 排序
			if (StringUtils.isNullOrEmpty(orderBy)) {
				orderBy = "id desc";
			}

			PageHelper.orderBy(orderBy);

			List<T> list = mapper.select(t);

			if (list != null) {
				pager = new PageInfo<T>(list);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("分页查询多条" + this.getTableName() + "信息失败！", ex);
		}

		return pager;
	}

	/**
	 * 根据自由构建条件分页查询(setPage()，setRows())，默认每页10条记录
	 * 
	 * @param p
	 *            Example，构建自 new Example(T.class);
	 * @param page
	 *            页码，从1开始
	 * @param rows
	 *            每页记录数，默认10
	 * @param orderBy
	 * @return
	 * @throws BusinessException
	 */
	public PageInfo<T> getPageExt(Example p, Integer page, Integer rows,
			String orderBy) {

		PageInfo<T> pager = null;

		try {
			// 分页，page默认下标从1开始， rows每页返回行数
			if (page != null && rows != null) {
				PageHelper.startPage(page, rows);
			} else {
				PageHelper.startPage(1, 10);
			}

			// 排序
			if (StringUtils.isNullOrEmpty(orderBy)) {
				orderBy = "id desc";
			}

			PageHelper.orderBy(orderBy);

			List<T> list = mapper.selectByExample(p);

			if (list != null) {
				pager = new PageInfo<T>(list);
			}
		} catch (Exception ex) {
			logger.error("分页查询多条" + this.getTableName() + "信息失败！", ex);
		}

		return pager;
	}

	/**
	 * 根据ID查询用户帐号信息
	 * 
	 * @param id
	 *            主键ID
	 * @return
	 */
	public T getById(Long id) {
		T result = null;

		try {
			result = mapper.selectByPrimaryKey(id);
		} catch (Exception ex) {
			logger.error("根据ID查询单条" + this.getTableName() + "信息失败！", ex);
		}

		return result;
	}

	/**
	 * 根据ID列表查询用户帐号信息
	 * 
	 * @param ids
	 *            主键ID，用逗号分隔。例如：1,2,3,4,5
	 * @return
	 */
	public List<T> getByIds(String ids) {
		List<T> result = null;

		try {
			result = mapper.selectByIds(ids);
		} catch (Exception ex) {
			logger.error("根据IDs查询多条" + this.getTableName() + "信息失败！", ex);
		}

		return result;
	}

	/**
	 * 根据ID列表查询用户帐号信息
	 * 
	 * @param ids
	 *            主键ID，用逗号分隔。例如：1,2,3,4,5
	 * @return
	 */
	public List<T> getByIds(String[] ids) {
		String idsString = StringUtil.join(ids, ',');
		return this.getByIds(idsString);
	}

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 *            主键ID
	 * @throws BusinessException
	 */
	public boolean deleteById(Long id) throws BusinessException {
		try {
			int effectRows = mapper.deleteByPrimaryKey(id);
			return effectRows > 0;
		} catch (Exception ex) {
			logger.error("根据ID删除单条" + this.getTableName() + "信息失败！", ex);
			throw new BusinessException("根据ID删除单条" + this.getTableName()
					+ "信息失败！");
		}
	}

	/**
	 * 更新或新增（更新时，根据主键更新实体全部字段，null值会被更新；如果只是更新某几个字段，请使用update()方法）
	 * 
	 * @param t
	 * @throws BusinessException
	 */
	// @Transactional(rollbackFor = BusinessException.class) //如果需要事务回滚，使用该注解即可。
	public boolean save(T t) throws BusinessException {
		try {
			int effectRows = 0;

			if (t.getId() != null) {
				effectRows = mapper.updateByPrimaryKey(t);
			} else {
				effectRows = mapper.insertSelective(t);
			}

			return effectRows > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			String type = t.getId() != null ? "保存" : "新建";
			logger.error(type + "单条" + this.getTableName() + "信息失败！", ex);
			throw new BusinessException(type + "单条" + this.getTableName()
					+ "信息失败！");
		}
	}

	/**
	 * 只更新值不为NULL的字段。
	 * 
	 * @param t
	 * @return
	 * @throws BusinessException
	 */
	public boolean update(T t) throws BusinessException {
		try {
			int effectRows = 0;

			if (t.getId() != null) {
				effectRows = mapper.updateByPrimaryKeySelective(t);
			}

			return effectRows > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			String type = "保存";
			logger.error(type + "单条" + this.getTableName() + "信息失败！", ex);
			throw new BusinessException(type + "单条" + this.getTableName()
					+ "信息失败！");
		}
	}

	/**
	 * 批量插入记录
	 * 
	 * @param list
	 * @return
	 * @throws BusinessException
	 */
	public boolean insertList(List<T> list) throws BusinessException {
		try {
			int effectRows = 0;

			if (list != null && list.size() > 0) {
				effectRows = mapper.insertList(list);
			}

			return effectRows > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			String type = "插入";
			logger.error("批量" + type + this.getTableName() + "信息失败！", ex);
			throw new BusinessException("批量" + type + this.getTableName()
					+ "信息失败！");
		}
	}

	/**
	 * 根据Id数组批量删除
	 * 
	 * @param ids
	 *            用英文逗号分隔，例如 ["1","2","3","4","5"]
	 * @return
	 * @throws BusinessException
	 */
	public boolean deleteByIds(String[] ids) throws BusinessException {
		String idsString = StringUtil.join(ids, ',');
		return this.deleteByIds(idsString);
	}

	/**
	 * 根据列表记录批量删除
	 * 
	 * @param recordList
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public boolean deleteByIds(List<T> recordList) throws BusinessException {
		String[] ids = new String[] {};
		List<String> idList = new ArrayList<String>();
		if (recordList != null) {
			for (T t : recordList) {
				idList.add(String.valueOf(t.getId()));
			}
		}
		return this.deleteByIds(idList.toArray(ids));
	}

	/**
	 * 根据Id批量删除
	 * 
	 * @param ids
	 *            用英文逗号分隔，例如 1,2,3,4,5
	 * @return
	 * @throws BusinessException
	 */
	public boolean deleteByIds(String ids) throws BusinessException {

		try {
			int effectRows = mapper.deleteByIds(ids);

			return effectRows > 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			String type = "删除";
			logger.error("批量" + type + this.getTableName() + "信息失败！", ex);
			throw new BusinessException("批量" + type + this.getTableName()
					+ "信息失败！");
		}
	}
}
