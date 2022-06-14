package by.bsu.soroka.lab.server.dao.xml.connection;

import by.bsu.soroka.lab.common.entity.Identifiable;
import by.bsu.soroka.lab.common.entity.Product;
import by.bsu.soroka.lab.common.entity.Stock;
import by.bsu.soroka.lab.common.entity.Storage;
import by.bsu.soroka.lab.server.dao.exception.DAOException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(namespace = "data")
@XmlSeeAlso({Product.class, Storage.class, Stock.class})
@XmlType(propOrder = {"counter", "data"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DataContainer<T extends Identifiable> {
    private int counter;

    @XmlElementWrapper(name = "data")
    @XmlElements(
            {
                    @XmlElement(name = "product", type = Product.class),
                    @XmlElement(name = "stock", type = Stock.class),
                    @XmlElement(name = "storage", type = Storage.class)
            }
    )
    private List<T> data = new ArrayList<>();

    @XmlTransient
    private JAXBContext jaxbContext;

    public DataContainer() throws DAOException {
        try {
            jaxbContext = JAXBContext.newInstance(DataContainer.class);
        } catch (JAXBException e) {
            throw new DAOException("backend error");
        }
    }

    public void write(String path) throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(this, new File(path));
    }

    public void reload(String path) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        DataContainer<T> temp = (DataContainer<T>) jaxbUnmarshaller.unmarshal(new File(path));
        this.counter = temp.counter;
        this.data = temp.data;
        if(this.data == null){
            this.data = new ArrayList<>();
        }
    }

    public List<T> getData(){
        return data;
    }
    public int incCounter(){
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

    public int add(T t){
        incCounter();
        data.add(t);
        return getCounter();
    }
}
