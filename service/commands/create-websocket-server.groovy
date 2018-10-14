@Command(name = 'create-websocket-server', description = 'Creates a Websocket server')
@PicocliScript GroovyScriptCommand me

@Parameters(paramLabel = "SERVER-NAME", description = 'The name of the server to create')
@Field String clientName

@Option(names = ['-f', '--force'], description = 'Whether to overwrite existing files')
@Field boolean overwrite

@Option(names = ['-l', '--lang'], description = 'The language used for the client (options: ${COMPLETION-CANDIDATES})')
@Field SupportedLanguage lang

@Mixin
@Field CommonOptionsMixin autoHelp // adds help, version and other common options to the command

private SupportedLanguage sniffProjectLanguage() {
    if (file("src/main/groovy").exists()) {
        SupportedLanguage.groovy
    } else if (file("src/main/kotlin").exists()) {
        SupportedLanguage.kotlin
    } else {
        SupportedLanguage.java
    }
}

def model = model(clientName).forConvention("Server")
String artifactPath = "${model.packagePath}/${model.className}"
lang = lang ?: SupportedLanguage.findValue(config.sourceLanguage).orElse(sniffProjectLanguage())

render template: template("${lang}/WebsocketServer.${lang.extension}"),
        destination: file("src/main/${lang}/${artifactPath}.${lang.extension}"),
        model: model,
        overwrite: overwrite