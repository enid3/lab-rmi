package by.bsu.soroka.lab.common.service;


import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Storage;

import java.rmi.Remote;

/**
 * @author Soroka Egor
 * Service, that describes operations with {@link Storage} data.
 * See {@link BasicService}
 */
public interface StorageService extends Remote, BasicService<Storage> {
}
