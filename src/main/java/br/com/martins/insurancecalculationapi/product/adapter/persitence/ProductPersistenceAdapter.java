package br.com.martins.insurancecalculationapi.product.adapter.persitence;

import br.com.martins.insurancecalculationapi.common.exception.DataConstraintViolationException;
import br.com.martins.insurancecalculationapi.product.entity.Product;
import br.com.martins.insurancecalculationapi.product.port.ProductOutputPort;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistenceAdapter implements ProductOutputPort {


    private final ProductRepository productRepository;

    public ProductPersistenceAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            if (dataIntegrityViolationException.getCause() instanceof ConstraintViolationException) {
                String constraintName = ((ConstraintViolationException) dataIntegrityViolationException.getCause()).getConstraintName();

                throw new DataConstraintViolationException(dataIntegrityViolationException.getCause().getMessage(),
                        dataIntegrityViolationException.getCause(), constraintName);
            }
            throw new DataConstraintViolationException(dataIntegrityViolationException.getCause().getMessage(),
                    dataIntegrityViolationException.getCause(), "");
        }
    }

    public Optional<Product> findById(UUID id){
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
