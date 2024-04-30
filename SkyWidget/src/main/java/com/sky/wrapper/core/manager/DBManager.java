package com.sky.wrapper.core.manager;//package com.easyder.wrapper.core.manager;
//
//import android.support.annotation.Nullable;
//
//import com.easyder.wrapper.core.dao.DaoMaster;
//import com.easyder.wrapper.core.dao.DaoSession;
//import com.easyder.wrapper.core.model.Entity;
//import com.easyder.wrapper.utils.UIUtils;
//
//import org.greenrobot.greendao.AbstractDao;
//import org.greenrobot.greendao.Property;
//import org.greenrobot.greendao.query.QueryBuilder;
//import org.greenrobot.greendao.query.WhereCondition;
//
//import java.io.Serializable;
//import java.lang.reflect.Field;
//import java.util.Collection;
//import java.util.List;
//
///**
// * @author 刘琛慧
// *         date 2016/6/20.
// */
//public class DBManager {
//
//    private final String dbName = "restaurant_db";
//    private static DBManager mInstance;
//    private DaoMaster.DevOpenHelper openHelper;
//    private DaoSession daoSession;
//
//    private DBManager() {
//        openHelper = new DaoMaster.DevOpenHelper(UIUtils.getContext(), dbName);
//        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
//        daoSession = daoMaster.newSession();
//    }
//
//    /**
//     * 获取单例引用
//     */
//    public static DBManager getInstance() {
//        if (mInstance == null) {
//            synchronized (DBManager.class) {
//                if (mInstance == null) {
//                    mInstance = new DBManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public <T extends Entity> QueryBuilder<T> getQueryBuilder(Class<? extends Entity> clazz) {
//        AbstractDao<Entity, ?> targetDao = getTargetDao(clazz);
//        if (targetDao == null) {
//            return null;
//        }
//
//        return (QueryBuilder<T>) targetDao.queryBuilder();
//    }
//
//    public <T extends Entity> List<T> loadByKeys(Class<? extends Entity> dataClass, Serializable[] keys) {
//        AbstractDao<Entity, ?> targetDao = getTargetDao(dataClass);
//        Property property = targetDao.getPkProperty();
//        QueryBuilder queryBuilder = targetDao.queryBuilder();
//        WhereCondition whereCondition;
//
//        if (keys.length > 2) {
//            WhereCondition[] whereConditions = new WhereCondition[keys.length - 2];
//            for (int i = 2; i < keys.length; i++) {
//                whereConditions[i] = property.eq(keys[i]);
//            }
//            whereCondition = queryBuilder.or(property.eq(keys[0]), property.eq(keys[1]), whereConditions);
//        } else {
//            whereCondition = queryBuilder.or(property.eq(keys[0]), property.eq(keys[1]));
//        }
//
//        return queryBuilder.where(whereCondition).list();
//    }
//
//    public <T extends Entity> List<T> loadAll(Class<? extends Entity> clazz, int offset) {
//        AbstractDao<Entity, ?> targetDao = getTargetDao(clazz);
//        return targetDao == null ? null : (List<T>) targetDao.queryBuilder().limit(10).offset(offset).list();
//    }
//
//
//    public <T extends Entity> T load(Class<? extends Entity> clazz, Serializable key) {
//        AbstractDao<?, Serializable> targetDao = (AbstractDao<?, Serializable>) getTargetDao(clazz);
//        if (targetDao == null) {
//            return null;
//        }
//
//        return (T) targetDao.load(key);
//    }
//
//    public <T extends Entity> List<T> loadBycondition(Class<? extends Entity> clazz, WhereCondition condition) {
//        AbstractDao<?, Serializable> targetDao = (AbstractDao<?, Serializable>) getTargetDao(clazz);
//        if (targetDao == null) {
//            return null;
//        }
//
//        return (List<T>) targetDao.queryBuilder().where(condition).list();
//    }
//
//
//    @Nullable
//    public <T extends Entity> AbstractDao<T, ? extends Serializable> getTargetDao(Class<? extends Entity> clazz) {
//        if (clazz == null) {
//            throw new IllegalArgumentException("class 不能为空");
//        }
//
//        Field field = null;
//        try {
//            field = clazz.getDeclaredField("myDao");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//
//        if (field == null) {
//            return null;
//        }
//
//        AbstractDao<?, ?> targetDao = null;
//        Collection<AbstractDao<?, ?>> allDaos = daoSession.getAllDaos();
//
//        for (AbstractDao<?, ?> dao : allDaos) {
//            if (dao.getClass().getSimpleName().equals(field.getType().getSimpleName())) {
//                targetDao = dao;
//                break;
//            }
//        }
//        return (AbstractDao<T, ? extends Serializable>) targetDao;
//    }
//
//    public void save(Entity entity) {
//        if (entity == null) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = (AbstractDao<Entity, ?>) getTargetDao(entity.getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.insert(entity);
//    }
//
//    public void saveAll(List<? extends Entity> entities) {
//        if (entities == null) {
//            throw new IllegalArgumentException("保存对象数据不能为空！");
//        } else if (entities.isEmpty()) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = (AbstractDao<Entity, ?>) getTargetDao(entities.get(0).getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.insertInTx((Iterable<Entity>) entities);
//    }
//
//    public void saveOrUpdate(Entity entity) {
//        if (entity == null) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = (AbstractDao<Entity, ?>) getTargetDao(entity.getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.insertOrReplace(entity);
//    }
//
//    public void saveOrUpdateAll(List<? extends Entity> entities) {
//        if (entities == null) {
//            throw new IllegalArgumentException("保存对象数据不能为空！");
//        } else if (entities.isEmpty()) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = getTargetDao(entities.get(0).getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.insertOrReplaceInTx((Iterable<Entity>) entities);
//    }
//
//    public void delete(Entity entity) {
//        if (entity == null) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = (AbstractDao<Entity, ?>) getTargetDao(entity.getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.delete(entity);
//    }
//
//
//    public void deleteByKey(Class<? extends Entity> clazz, Serializable key) {
//        if (clazz == null || key == null) {
//            return;
//        }
//
//        AbstractDao<Entity, Serializable> targetDao = (AbstractDao<Entity, Serializable>) getTargetDao(clazz);
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.deleteByKey(key);
//    }
//
//    public void deleteByKeys(Class<? extends Entity> clazz, List<Serializable> keys) {
//        if (clazz == null || keys == null || keys.isEmpty()) {
//            return;
//        }
//
//        AbstractDao<Entity, Serializable> targetDao = (AbstractDao<Entity, Serializable>) getTargetDao(clazz);
//
//        if (targetDao == null) {
//            return;
//        }
//
//
//        targetDao.deleteByKeyInTx(keys);
//    }
//
//
//    public void deleteByKeys(Class<? extends Entity> clazz, Serializable... keys) {
//        if (clazz == null || keys == null || keys.length == 0) {
//            return;
//        }
//
//        AbstractDao<Entity, Serializable> targetDao = (AbstractDao<Entity, Serializable>) getTargetDao(clazz);
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.deleteByKeyInTx(keys);
//    }
//
//    public void deleteAll(List<? extends Entity> entities) {
//        if (entities == null) {
//            throw new IllegalArgumentException("保存对象数据不能为空！");
//        } else if (entities.isEmpty()) {
//            return;
//        }
//
//        AbstractDao<Entity, ?> targetDao = (AbstractDao<Entity, ?>) getTargetDao(entities.get(0).getClass());
//
//        if (targetDao == null) {
//            return;
//        }
//
//        targetDao.deleteInTx((Iterable<Entity>) entities);
//    }
//
//
//    public DaoSession getDaoSession() {
//        return daoSession;
//    }
//
//    public void setDaoSession(DaoSession daoSession) {
//        this.daoSession = daoSession;
//    }
//
//    /**
//     * Activity或者Fragment销毁,关闭Realm数据库
//     */
//    public void destroy() {
//
//
//    }
//
//
//}
//
