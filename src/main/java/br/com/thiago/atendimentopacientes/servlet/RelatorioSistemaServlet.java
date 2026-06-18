package br.com.thiago.atendimentopacientes.servlet;

import br.com.thiago.atendimentopacientes.model.Atendimento;
import br.com.thiago.atendimentopacientes.model.Paciente;
import br.com.thiago.atendimentopacientes.service.AtendimentoService;
import br.com.thiago.atendimentopacientes.service.PacienteService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioSistemaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PacienteService pacienteService;
    private final AtendimentoService atendimentoService;

    public RelatorioSistemaServlet(PacienteService pacienteService, AtendimentoService atendimentoService) {
        this.pacienteService = pacienteService;
        this.atendimentoService = atendimentoService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Paciente> pacientes = pacienteService.listarTodos();
        List<Atendimento> atendimentos = atendimentoService.listarTodos();

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            String contextPath = request.getContextPath();

            writer.println("<!DOCTYPE html>");
            writer.println("<html lang=\"pt-BR\">");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>Relatório do Sistema</title>");
            writer.println("<link rel=\"stylesheet\" href=\"" + contextPath
                    + "/jakarta.faces.resource/style.css.xhtml?ln=css\" />");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<header class=\"topbar\">");
            writer.println("<h1>Sistema de Atendimento de Pacientes</h1>");
            writer.println("<nav class=\"menu\">");
            writer.println("<a href=\"" + contextPath + "/index.xhtml\">Início</a>");
            writer.println("<a href=\"" + contextPath + "/pacientes/listar.xhtml\">Pacientes</a>");
            writer.println("<a href=\"" + contextPath + "/atendimentos/listar.xhtml\">Atendimentos</a>");
            writer.println("<a href=\"" + contextPath + "/relatorio-sistema\">Relatório</a>");
            writer.println("</nav>");
            writer.println("</header>");

            writer.println("<main class=\"container\">");
            writer.println("<section class=\"page-header\">");
            writer.println("<h2>Relatório do Sistema</h2>");
            writer.println("<p>Resumo geral de pacientes e atendimentos cadastrados.</p>");
            writer.println("</section>");

            writer.println("<section class=\"panel\">");
            writer.println("<div class=\"filter-row\">");
            writer.println("<div class=\"field\">");
            writer.println("<label>Total de pacientes cadastrados</label>");
            writer.println("<p class=\"empty-message\">" + pacientes.size() + "</p>");
            writer.println("</div>");
            writer.println("<div class=\"field\">");
            writer.println("<label>Total de atendimentos cadastrados</label>");
            writer.println("<p class=\"empty-message\">" + atendimentos.size() + "</p>");
            writer.println("</div>");
            writer.println("<div class=\"field\">");
            writer.println("<label>Data e hora de geração</label>");
            writer.println("<p class=\"empty-message\">" + texto(LocalDateTime.now().format(DATA_HORA_FORMATTER)) + "</p>");
            writer.println("</div>");
            writer.println("<a class=\"button secondary\" href=\"" + contextPath + "/index.xhtml\">Voltar para a página inicial</a>");
            writer.println("</div>");
            writer.println("</section>");

            escreverTabelaPacientes(writer, pacientes);
            escreverTabelaAtendimentos(writer, atendimentos);

            writer.println("</main>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }

    private void escreverTabelaPacientes(PrintWriter writer, List<Paciente> pacientes) {
        writer.println("<section class=\"panel\">");
        writer.println("<h2>Pacientes</h2>");

        if (pacientes.isEmpty()) {
            writer.println("<p class=\"empty-message\">Nenhum paciente cadastrado.</p>");
            writer.println("</section>");
            return;
        }

        writer.println("<table class=\"data-table\">");
        writer.println("<thead>");
        writer.println("<tr>");
        writer.println("<th>ID</th>");
        writer.println("<th>Nome</th>");
        writer.println("<th>CPF</th>");
        writer.println("<th>Cidade</th>");
        writer.println("</tr>");
        writer.println("</thead>");
        writer.println("<tbody>");

        for (Paciente paciente : pacientes) {
            writer.println("<tr>");
            writer.println("<td>" + texto(paciente.getId()) + "</td>");
            writer.println("<td>" + texto(paciente.getNome()) + "</td>");
            writer.println("<td>" + texto(paciente.getCpf()) + "</td>");
            writer.println("<td>" + texto(paciente.getCidade()) + "</td>");
            writer.println("</tr>");
        }

        writer.println("</tbody>");
        writer.println("</table>");
        writer.println("</section>");
    }

    private void escreverTabelaAtendimentos(PrintWriter writer, List<Atendimento> atendimentos) {
        writer.println("<section class=\"panel\">");
        writer.println("<h2>Atendimentos</h2>");

        if (atendimentos.isEmpty()) {
            writer.println("<p class=\"empty-message\">Nenhum atendimento cadastrado.</p>");
            writer.println("</section>");
            return;
        }

        writer.println("<table class=\"data-table\">");
        writer.println("<thead>");
        writer.println("<tr>");
        writer.println("<th>ID</th>");
        writer.println("<th>Data</th>");
        writer.println("<th>Status</th>");
        writer.println("<th>Descrição</th>");
        writer.println("<th>Paciente</th>");
        writer.println("</tr>");
        writer.println("</thead>");
        writer.println("<tbody>");

        for (Atendimento atendimento : atendimentos) {
            writer.println("<tr>");
            writer.println("<td>" + texto(atendimento.getId()) + "</td>");
            writer.println("<td>" + texto(formatarData(atendimento)) + "</td>");
            writer.println("<td>" + texto(atendimento.getStatus()) + "</td>");
            writer.println("<td>" + texto(atendimento.getDescricao()) + "</td>");
            writer.println("<td>" + texto(nomePaciente(atendimento)) + "</td>");
            writer.println("</tr>");
        }

        writer.println("</tbody>");
        writer.println("</table>");
        writer.println("</section>");
    }

    private String formatarData(Atendimento atendimento) {
        if (atendimento.getDataAtendimento() == null) {
            return "";
        }

        return atendimento.getDataAtendimento().format(DATA_FORMATTER);
    }

    private String nomePaciente(Atendimento atendimento) {
        if (atendimento.getPaciente() == null) {
            return "";
        }

        return atendimento.getPaciente().getNome();
    }

    private String texto(Object valor) {
        if (valor == null) {
            return "";
        }

        return valor.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
