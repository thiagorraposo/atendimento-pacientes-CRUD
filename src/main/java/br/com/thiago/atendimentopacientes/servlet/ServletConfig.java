package br.com.thiago.atendimentopacientes.servlet;

import br.com.thiago.atendimentopacientes.service.AtendimentoService;
import br.com.thiago.atendimentopacientes.service.PacienteService;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<RelatorioSistemaServlet> relatorioSistemaServlet(
            PacienteService pacienteService,
            AtendimentoService atendimentoService) {

        ServletRegistrationBean<RelatorioSistemaServlet> registrationBean = new ServletRegistrationBean<>(
                new RelatorioSistemaServlet(pacienteService, atendimentoService),
                "/relatorio-sistema");
        registrationBean.setName("relatorioSistemaServlet");
        return registrationBean;
    }
}
