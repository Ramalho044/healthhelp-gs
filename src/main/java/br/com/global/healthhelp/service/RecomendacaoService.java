package br.com.global.healthhelp.service;

import br.com.global.healthhelp.dto.RecomendacaoDTO;
import br.com.global.healthhelp.model.Atividade;
import br.com.global.healthhelp.model.RegistroDiario;
import br.com.global.healthhelp.model.Usuario;
import br.com.global.healthhelp.repository.AtividadeRepository;
import br.com.global.healthhelp.repository.RecomendacaoRepository;
import br.com.global.healthhelp.repository.RegistroDiarioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecomendacaoService {

    private final RegistroDiarioRepository registroRepo;
    private final AtividadeRepository atividadeRepo;
    private final RecomendacaoRepository recomendacaoRepo;
    private final ChatClient chatClient;

    public RecomendacaoService(RegistroDiarioRepository registroRepo,
                               AtividadeRepository atividadeRepo,
                               RecomendacaoRepository recomendacaoRepo,
                               ChatClient chatClient) {
        this.registroRepo = registroRepo;
        this.atividadeRepo = atividadeRepo;
        this.recomendacaoRepo = recomendacaoRepo;
        this.chatClient = chatClient;
    }

    @Transactional
    public RecomendacaoDTO gerarRecomendacao(Usuario usuario) {

        List<RegistroDiario> registros =
                registroRepo.findByUsuario(usuario, Pageable.unpaged()).getContent();

        StringBuilder sb = new StringBuilder();
        sb.append("Você é um especialista em saúde e bem-estar.\n");
        sb.append("Analise a rotina abaixo e gere recomendações práticas, cuidadosas e realistas.\n");
        sb.append("Responda em português brasileiro, em tópicos.\n\n");

        for (RegistroDiario r : registros) {

            sb.append("Dia: ").append(r.getDataRef()).append("\n")
                    .append("Score: ").append(r.getPontuacaoEquilibrio()).append("\n")
                    .append("Observações: ").append(r.getObservacoes()).append("\n\n");

            // ⬇️ AGORA USANDO O NOME CERTO DO MÉTODO
            List<Atividade> atividades = atividadeRepo.findByRegistro(r);

            for (Atividade a : atividades) {
                sb.append("- ").append(a.getCategoria().getNome()).append("\n")
                        .append("   início: ").append(a.getInicio()).append("\n")
                        .append("   fim: ").append(a.getFim()).append("\n")
                        .append("   descrição: ").append(a.getDescricao()).append("\n")
                        .append("\n");
            }
        }

        String texto = chatClient
                .prompt()
                .user(sb.toString())
                .call()
                .content();

        return new RecomendacaoDTO(
                null,
                "Recomendações personalizadas de rotina",
                texto,
                null,
                LocalDateTime.now()
        );
    }
}
