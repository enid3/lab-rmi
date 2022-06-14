package by.bsu.soroka.lab.common.service;


import by.bsu.soroka.lab.common.entity.Product;

import java.rmi.Remote;


/**
 * @author Soroka Egor
 * Service, that describes operations with {@link Product} data.
 * See {@link BasicService}
 */
public interface ProductService extends Remote, BasicService<Product> {
}
