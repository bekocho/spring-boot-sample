package sample.data.elasticsearch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfiguration implements DisposableBean {

    private static Log logger = LogFactory.getLog(ElasticsearchConfiguration.class);

    @Autowired
    private ElasticsearchProperties properties;

    private NodeClient client;

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        return new ElasticsearchTemplate(esClient());
    }

    @Bean
    public Client esClient() {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Starting Elasticsearch client");
            }
            NodeBuilder nodeBuilder = new NodeBuilder();
            nodeBuilder
                    .clusterName(this.properties.getClusterName())
                    .local(false)
            ;
            nodeBuilder.settings()
                    .put("http.enabled", true)
            ;
            this.client = (NodeClient)nodeBuilder.node().client();
            return this.client;
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.client != null) {
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Closing Elasticsearch client");
                }
                if (this.client != null) {
                    this.client.close();
                }
            }
            catch (final Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error closing Elasticsearch client: ", ex);
                }
            }
        }
    }
}
