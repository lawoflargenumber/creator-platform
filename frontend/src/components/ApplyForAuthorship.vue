<template>

    <v-card outlined>
        <v-card-title>
            ApplyForAuthorship
        </v-card-title>

        <v-card-text>
            <Number label="Id" v-model="value.id" :editMode="editMode"/>
            <String label="AuthorsProfile" v-model="value.authorsProfile" :editMode="editMode"/>
            <String label="Autornickname" v-model="value.autornickname" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="applyForAuthorship"
            >
                ApplyForAuthorship
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'ApplyForAuthorshipCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.id = 0;
        this.value.authorsProfile = '';
        this.value.autornickname = '';
    },
    watch: {
    },
    methods: {
        applyForAuthorship() {
            this.$emit('applyForAuthorship', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

